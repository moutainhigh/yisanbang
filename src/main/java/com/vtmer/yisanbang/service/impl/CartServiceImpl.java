package com.vtmer.yisanbang.service.impl;

import com.alibaba.fastjson.JSON;
import com.vtmer.yisanbang.common.exception.service.cart.CartGoodsNotExistException;
import com.vtmer.yisanbang.common.util.ListSort;
import com.vtmer.yisanbang.domain.Cart;
import com.vtmer.yisanbang.domain.CartGoods;
import com.vtmer.yisanbang.domain.Discount;
import com.vtmer.yisanbang.domain.User;
import com.vtmer.yisanbang.dto.CartGoodsDTO;
import com.vtmer.yisanbang.dto.GoodsSkuDTO;
import com.vtmer.yisanbang.mapper.CartGoodsMapper;
import com.vtmer.yisanbang.mapper.CartMapper;
import com.vtmer.yisanbang.mapper.DiscountMapper;
import com.vtmer.yisanbang.mapper.UserMapper;
import com.vtmer.yisanbang.service.CartService;
import com.vtmer.yisanbang.service.ColorSizeService;
import com.vtmer.yisanbang.service.PartSizeService;
import com.vtmer.yisanbang.shiro.JwtFilter;
import com.vtmer.yisanbang.vo.CartVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    private PartSizeService partSizeService;

    @Autowired
    private ColorSizeService colorSizeService;

    @Autowired
    private DiscountMapper discountMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private CartGoodsMapper cartGoodsMapper;

    @Autowired
    private RedisTemplate<String,Object> stringRedisTemplate;

    private static final String REDIS_CART = "cart";

    // 满X件打折
    private int discountAmount;

    // 打折率
    private double discountRate;

    // 用户id
    private Integer userId;

    // redis key
    private String key;

    // hash操作对象
    private BoundHashOperations<String, Object, Object> hashOperations;

    // 设置优惠信息
    private void setDiscount() {
        Discount discount = discountMapper.select();
        if (discount !=null) {
            discountAmount = discount.getAmount();
            discountRate = discount.getDiscountRate();
        } else {  // 如果未设置优惠，则默认达标数量为0，优惠*1,，即无打折
            discountAmount = 0;
            discountRate = 1;
        }
    }

    private void getUserIdAndSetKey() {
        userId = JwtFilter.getLoginUser().getId();
        key = REDIS_CART + ":" + userId;
        hashOperations = stringRedisTemplate.boundHashOps(key);
    }

    @Override
    public CartVO selectCartVo() {
        getUserIdAndSetKey();
        if (!stringRedisTemplate.hasKey(key)) {
            // 不存在直接返回
            return null;
        }
        // 获取所有数据
        List<Object> ObjectList = hashOperations.values();
        // 判断是否有数据
        if (CollectionUtils.isEmpty(ObjectList)) {
            return null;
        }
        return convertObjectListToCartVo(ObjectList);
    }

    @Override
    public void addCartGoods(List<CartGoodsDTO> cartGoodsDTOList) {
        getUserIdAndSetKey();
        for (CartGoodsDTO cartGoodsDTO : cartGoodsDTOList) {
            // 更新时间
            cartGoodsDTO.setUpdateTime(System.currentTimeMillis());
            // 查询商品是否存在
            Integer colorSizeId = cartGoodsDTO.getColorSizeId();
            Boolean isGoods = cartGoodsDTO.getWhetherGoods();
            Integer amount = cartGoodsDTO.getAmount();
            Boolean result = hashOperations.hasKey(colorSizeId.toString()+isGoods.toString());
            assert result != null;
            if (result) {
                // 存在，获取购物车数据
                String json = Objects.requireNonNull(hashOperations.get(colorSizeId.toString() + isGoods.toString())).toString();
                cartGoodsDTO = JSON.parseObject(json, CartGoodsDTO.class);
                // 修改购物车商品数量
                cartGoodsDTO.setAmount(cartGoodsDTO.getAmount() + amount);
            } else {
                // 不存在，新增购物车数据
                cartGoodsDTO.setUserId(userId);
                // 默认为已勾选
                cartGoodsDTO.setWhetherChosen(true);
                // 设置商品信息
                if (isGoods) {
                    // 如果是普通商品
                    colorSizeService.setSkuById(cartGoodsDTO);
                } else {
                    // 如果是套装商品
                    partSizeService.setSkuById(cartGoodsDTO);
                }
            }
            // 将购物车数据写入redis
            hashOperations.put(colorSizeId.toString() + isGoods.toString(),JSON.toJSONString(cartGoodsDTO));
        } // end for
    }

    @Override
    public void recoveryCartData() {
        List<Cart> cartList = cartMapper.selectAll();
        if (cartList != null) {
            for (Cart cart : cartList) {
                int userId = cart.getUserId();
                key = REDIS_CART + ":" + userId;
                hashOperations = stringRedisTemplate.boundHashOps(key);
                List<CartGoods> cartGoodsDTOList = cartGoodsMapper.selectCartGoodsByCartId(cart.getId());
                for (CartGoods cartGoods : cartGoodsDTOList) {
                    CartGoodsDTO cartGoodsDTO = new CartGoodsDTO();
                    // 复制amount、colorSizeId、whetherGoods、whetherChosen进cartGoodsDTO
                    BeanUtils.copyProperties(cartGoods,cartGoodsDTO);
                    Integer colorSizeId = cartGoods.getColorSizeId();
                    Boolean whetherGoods = cartGoods.getWhetherGoods();
                    // 设置商品信息
                    if (whetherGoods) {
                        // 如果是普通商品
                        colorSizeService.setSkuById(cartGoodsDTO);
                    } else {
                        // 如果是套装商品
                        partSizeService.setSkuById(cartGoodsDTO);
                    }
                    // 将购物车数据写入redis
                    hashOperations.put(colorSizeId.toString() + whetherGoods.toString(),JSON.toJSONString(cartGoodsDTO));
                } // end 内层for
            } // end 外层for
        }

    }

    @Override
    @Transactional
    public void updateChosen(GoodsSkuDTO goodsSkuDto) {
        getUserIdAndSetKey();
        Boolean isGoods = goodsSkuDto.getWhetherGoods();
        Integer colorSizeId = goodsSkuDto.getColorSizeId();
        if (!hashOperations.hasKey(colorSizeId.toString() + isGoods.toString())) {
            // 如果不存在该商品
            throw new CartGoodsNotExistException("购物车商品不存在--用户id:"+ userId +"colorSizeId:"+ colorSizeId +"、isGoods:"+isGoods);
        }
        // 存在，获取购物车数据
        String json = Objects.requireNonNull(hashOperations.get(colorSizeId.toString() + isGoods.toString())).toString();
        CartGoodsDTO cartGoodsDto = JSON.parseObject(json, CartGoodsDTO.class);
        // 修改勾选
        Boolean isChosen = cartGoodsDto.getWhetherChosen();
        if (isChosen) {
            cartGoodsDto.setWhetherChosen(false);
        } else {
            cartGoodsDto.setWhetherChosen(true);
        }
        // 将购物车数据写入redis
        hashOperations.put(colorSizeId.toString() + isGoods.toString(),JSON.toJSONString(cartGoodsDto));
    }



    @Override
    @Transactional
    public void addOrSubtractAmount(CartGoodsDTO cartGoodsDto) {
        getUserIdAndSetKey();
        Boolean isGoods = cartGoodsDto.getWhetherGoods();
        Integer colorSizeId = cartGoodsDto.getColorSizeId();
        Integer amount = cartGoodsDto.getAmount();
        if (!hashOperations.hasKey(colorSizeId.toString() + isGoods.toString())) {
            // 如果不存在该商品
            throw new CartGoodsNotExistException("购物车商品不存在--用户id:"+ userId +"colorSizeId:"+ colorSizeId +"、isGoods:"+isGoods);
        }
        // 存在，获取购物车数据
        String json = Objects.requireNonNull(hashOperations.get(colorSizeId.toString() + isGoods.toString())).toString();
        cartGoodsDto = JSON.parseObject(json, CartGoodsDTO.class);
        // 增减购物车数量
        cartGoodsDto.setAmount(cartGoodsDto.getAmount() + amount);
        // 将购物车数据写入redis
        hashOperations.put(colorSizeId.toString() + isGoods.toString(),JSON.toJSONString(cartGoodsDto));
    }




    @Override
    @Transactional
    public void updateAmount(CartGoodsDTO cartGoodsDto) {
        getUserIdAndSetKey();
        Boolean isGoods = cartGoodsDto.getWhetherGoods();
        Integer colorSizeId = cartGoodsDto.getColorSizeId();
        Integer amount = cartGoodsDto.getAmount();
        if (!hashOperations.hasKey(colorSizeId.toString() + isGoods.toString())) {
            // 如果不存在该商品
            throw new CartGoodsNotExistException("购物车商品不存在--用户id:"+ userId +"colorSizeId:"+ colorSizeId +"、isGoods:"+isGoods);
        }
        // 存在，获取购物车数据
        String json = Objects.requireNonNull(hashOperations.get(colorSizeId.toString() + isGoods.toString())).toString();
        cartGoodsDto = JSON.parseObject(json, CartGoodsDTO.class);
        // 修改购物车数量
        cartGoodsDto.setAmount(amount);
        // 将购物车数据写入redis
        hashOperations.put(colorSizeId.toString() + isGoods.toString(),JSON.toJSONString(cartGoodsDto));
    }



    @Override
    @Transactional
    public void deleteCartGoods(List<GoodsSkuDTO> goodsSkuDTOList) {
        getUserIdAndSetKey();
        for (GoodsSkuDTO goodsSkuDto : goodsSkuDTOList) {
            Integer colorSizeId = goodsSkuDto.getColorSizeId();
            Boolean isGoods = goodsSkuDto.getWhetherGoods();
            if (!hashOperations.hasKey(colorSizeId.toString() + isGoods.toString())) {
                // 如果不存在该商品
                throw new CartGoodsNotExistException("购物车商品不存在--用户id:"+ userId +"colorSizeId:"+ colorSizeId +"、isGoods:"+isGoods);
            }
            // 删除商品
            hashOperations.delete(colorSizeId.toString() + isGoods.toString());
        }
    }


    /*
        根据购物车所有商品信息计算总价
    */
    @Override
    public Map<String,Double> calculateTotalPrice(List<CartGoodsDTO> cartGoodsList) {
        setDiscount();
        HashMap<String, Double> priceMap = new HashMap<>();
        // 优惠后的总价
        double totalPrice = 0;
        // 优惠前的总价
        double beforeTotalPrice = 0;
        for (CartGoodsDTO cartGoodsDto : cartGoodsList) {
            // 单项商品总价
            double price = 0;
            // 如果勾选了，计算总价
            if (cartGoodsDto.getWhetherChosen().equals(Boolean.TRUE)) {
                // 优惠前总价
                price = cartGoodsDto.getPrice() * cartGoodsDto.getAmount();
                cartGoodsDto.setTotalPrice(price);
                beforeTotalPrice += price;
                // 如果符合优惠,乘上打折率0.8
                if (cartGoodsDto.getAmount() >= discountAmount) {
                    price *=  discountRate;
                }
                cartGoodsDto.setAfterTotalPrice(price);
                // 优惠后总价
                totalPrice += price;
            }
        }
        priceMap.put("totalPrice",totalPrice);
        priceMap.put("beforeTotalPrice",beforeTotalPrice);
        return priceMap;
    }

    // 删除购物车中的勾选商品
    @Override
    public boolean deleteCartGoodsByIsChosen() {
        getUserIdAndSetKey();
        if (!stringRedisTemplate.hasKey(key)) {
            // 不存在直接返回
            return false;
        }
        // 获取所有数据
        List<Object> ObjectList = hashOperations.values();
        // 判断是否有数据
        if (CollectionUtils.isEmpty(ObjectList)) {
            return false;
        }
        // 查询购物车数据
        List<CartGoodsDTO> cartGoodsList;
        cartGoodsList = ObjectList.stream().map(o -> JSON.parseObject(o.toString(), CartGoodsDTO.class)).collect(Collectors.toList());
        // 删除其中已勾选的购物车商品
        for (CartGoodsDTO cartGoodsDto : cartGoodsList) {
            if (cartGoodsDto.getWhetherChosen()) {
                // 如果是已勾选的，从redis中删除之
                hashOperations.delete(cartGoodsDto.getColorSizeId().toString() + cartGoodsDto.getWhetherGoods().toString());
            }
        }
        return true;
    }

    /**
     * 购物车数据持久化到数据库
     */
    @Override
    @Transactional
    public void cartDataPersistence() {
        List<User> userList = userMapper.selectAll();
        for (User user : userList) {
            int userId = user.getId();
            key = REDIS_CART + ":" + userId;
            hashOperations = stringRedisTemplate.boundHashOps(key);
            if (stringRedisTemplate.hasKey(key)) {
                // 存在key
                // 获取所有数据
                List<Object> ObjectList = hashOperations.values();
                // 判断是否有数据
                if (!CollectionUtils.isEmpty(ObjectList)) {
                    // 有数据
                    CartVO cartVo = convertObjectListToCartVo(ObjectList);
                    // 封装cart并插入
                    Cart cart = new Cart();
                    cart.setUserId(userId);
                    cart.setTotalPrice(cartVo.getTotalPrice());
                    cartMapper.insert(cart);
                    List<CartGoodsDTO> cartGoodsList = cartVo.getCartGoodsList();
                    for (CartGoodsDTO cartGoodsDTO : cartGoodsList) {
                        CartGoods cartGoods = new CartGoods();
                        cartGoods.setCartId(cart.getId());
                        BeanUtils.copyProperties(cartGoodsDTO,cartGoods);
                        // 插入购物车商品列表
                        cartGoodsMapper.insert(cartGoods);
                    }
                } // end if 有无数据
            } // end if 是否存在key
        } // end for
    }


    /**
     * 由redis获取到某用户的所有购物车商品Object列表转换为CartVo
     * @param ObjectList
     * @return
     */
    private CartVO convertObjectListToCartVo(List<Object> ObjectList) {
        // 查询购物车数据
        List<CartGoodsDTO> cartGoodsList;
        cartGoodsList = ObjectList.stream().map(o -> JSON.parseObject(o.toString(), CartGoodsDTO.class)).collect(Collectors.toList());
        // 计算总价
        Map<String, Double> priceMap = calculateTotalPrice(cartGoodsList);
        CartVO cartVo = new CartVO();
        // 设置总价
        cartVo.setTotalPrice(priceMap.get("totalPrice"));
        cartVo.setBeforeTotalPrice(priceMap.get("beforeTotalPrice"));
        // 根据时间排序
        ListSort.listTimeSort(cartGoodsList);
        cartVo.setCartGoodsList(cartGoodsList);
        return cartVo;
    }
}
