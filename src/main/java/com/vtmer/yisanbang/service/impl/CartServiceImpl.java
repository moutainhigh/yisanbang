package com.vtmer.yisanbang.service.impl;

import com.alibaba.fastjson.JSON;
import com.vtmer.yisanbang.common.JedisClient;
import com.vtmer.yisanbang.common.ListSort;
import com.vtmer.yisanbang.common.TokenInterceptor;
import com.vtmer.yisanbang.domain.Discount;
import com.vtmer.yisanbang.dto.CartGoodsDto;
import com.vtmer.yisanbang.dto.GoodsSkuDto;
import com.vtmer.yisanbang.mapper.CartGoodsMapper;
import com.vtmer.yisanbang.mapper.CartMapper;
import com.vtmer.yisanbang.mapper.DiscountMapper;
import com.vtmer.yisanbang.service.CartService;
import com.vtmer.yisanbang.service.ColorSizeService;
import com.vtmer.yisanbang.service.PartSizeService;
import com.vtmer.yisanbang.vo.CartVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    private PartSizeService partSizeService;

    @Autowired
    private ColorSizeService colorSizeService;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private CartGoodsMapper cartGoodsMapper;

    @Autowired
    private DiscountMapper discountMapper;

    @Autowired
    private JedisClient jedisClient;

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
    BoundHashOperations<String, Object, Object> hashOperations;

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
        userId = TokenInterceptor.getLoginUser().getId();
        key = REDIS_CART + ":" + userId;
        hashOperations = stringRedisTemplate.boundHashOps(key);
    }

    @Override
    public CartVo selectCartVo() {
        getUserIdAndSetKey();
        if (!stringRedisTemplate.hasKey(key)) {
            // 不存在直接返回
            return null;
        }
        // 获取hash的操作对象
        List<Object> ObjectList = hashOperations.values();
        // 判断是否有数据
        if (CollectionUtils.isEmpty(ObjectList)) {
            return null;
        }
        // 查询购物车数据
        List<CartGoodsDto> cartGoodsList;
        cartGoodsList = ObjectList.stream().map(o -> JSON.parseObject(o.toString(), CartGoodsDto.class)).collect(Collectors.toList());
        // 计算总价
        Map<String, Double> priceMap = calculateTotalPrice(cartGoodsList);
        CartVo cartVo = new CartVo();
        // 设置总价
        cartVo.setTotalPrice(priceMap.get("totalPrice"));
        cartVo.setBeforeTotalPrice(priceMap.get("beforeTotalPrice"));
        // 根据时间排序
        ListSort.listTimeSort(cartGoodsList);
        cartVo.setCartGoodsList(cartGoodsList);
        return cartVo;
    }

    public void addCartGoods(List<CartGoodsDto> cartGoodsDtoList) {
        getUserIdAndSetKey();

        for (CartGoodsDto cartGoodsDto : cartGoodsDtoList) {
            // 更新时间
            cartGoodsDto.setUpdateTime(System.currentTimeMillis());
            // 查询商品是否存在
            Integer colorSizeId = cartGoodsDto.getColorSizeId();
            Boolean isGoods = cartGoodsDto.getIsGoods();
            Integer amount = cartGoodsDto.getAmount();
            Boolean result = hashOperations.hasKey(colorSizeId.toString()+isGoods.toString());
            assert result != null;
            if (result) {
                // 存在，获取购物车数据
                String json = Objects.requireNonNull(hashOperations.get(colorSizeId.toString() + isGoods.toString())).toString();
                cartGoodsDto = JSON.parseObject(json, CartGoodsDto.class);
                // 修改购物车商品数量
                cartGoodsDto.setAmount(cartGoodsDto.getAmount() + amount);
            } else {
                // 不存在，新增购物车数据
                cartGoodsDto.setUserId(userId);
                // 默认为已勾选
                cartGoodsDto.setIsChosen(true);
                // 设置商品信息
                if (isGoods) {
                    // 如果是普通商品
                    colorSizeService.setSkuById(cartGoodsDto);
                } else {
                    // 如果是套装商品
                    partSizeService.setSkuById(cartGoodsDto);
                }
            }
            // 将购物车数据写入redis
            hashOperations.put(colorSizeId.toString() + isGoods.toString(),JSON.toJSONString(cartGoodsDto));
        } // end for
    }


    @Transactional
    public boolean updateChosen(GoodsSkuDto goodsSkuDto) {
        getUserIdAndSetKey();
        Boolean isGoods = goodsSkuDto.getIsGoods();
        Integer colorSizeId = goodsSkuDto.getColorSizeId();
        if (!hashOperations.hasKey(colorSizeId.toString() + isGoods.toString())) {
            // 如果不存在该商品
            return false;
        }
        // 存在，获取购物车数据
        String json = Objects.requireNonNull(hashOperations.get(colorSizeId.toString() + isGoods.toString())).toString();
        CartGoodsDto cartGoodsDto = JSON.parseObject(json, CartGoodsDto.class);
        // 修改勾选
        Boolean isChosen = cartGoodsDto.getIsChosen();
        if (isChosen) {
            cartGoodsDto.setIsChosen(false);
        } else {
            cartGoodsDto.setIsChosen(true);
        }
        // 将购物车数据写入redis
        hashOperations.put(colorSizeId.toString() + isGoods.toString(),JSON.toJSONString(cartGoodsDto));
        return true;
    }



    @Transactional
    public boolean addOrSubtractAmount(CartGoodsDto cartGoodsDto) {
        getUserIdAndSetKey();
        Boolean isGoods = cartGoodsDto.getIsGoods();
        Integer colorSizeId = cartGoodsDto.getColorSizeId();
        Integer amount = cartGoodsDto.getAmount();
        if (!hashOperations.hasKey(colorSizeId.toString() + isGoods.toString())) {
            // 如果不存在该商品
            return false;
        }
        // 存在，获取购物车数据
        String json = Objects.requireNonNull(hashOperations.get(colorSizeId.toString() + isGoods.toString())).toString();
        cartGoodsDto = JSON.parseObject(json, CartGoodsDto.class);
        // 增减购物车数量
        cartGoodsDto.setAmount(cartGoodsDto.getAmount() + amount);
        // 将购物车数据写入redis
        hashOperations.put(colorSizeId.toString() + isGoods.toString(),JSON.toJSONString(cartGoodsDto));
        return true;
    }




    @Override
    @Transactional
    public boolean updateAmount(CartGoodsDto cartGoodsDto) {
        getUserIdAndSetKey();
        Boolean isGoods = cartGoodsDto.getIsGoods();
        Integer colorSizeId = cartGoodsDto.getColorSizeId();
        Integer amount = cartGoodsDto.getAmount();
        if (!hashOperations.hasKey(colorSizeId.toString() + isGoods.toString())) {
            // 如果不存在该商品
            return false;
        }
        // 存在，获取购物车数据
        String json = Objects.requireNonNull(hashOperations.get(colorSizeId.toString() + isGoods.toString())).toString();
        cartGoodsDto = JSON.parseObject(json, CartGoodsDto.class);
        // 修改购物车数量
        cartGoodsDto.setAmount(amount);
        // 将购物车数据写入redis
        hashOperations.put(colorSizeId.toString() + isGoods.toString(),JSON.toJSONString(cartGoodsDto));
        return true;
    }



    @Override
    @Transactional
    public Boolean deleteCartGoods(List<GoodsSkuDto> goodsSkuDtoList) {
        getUserIdAndSetKey();
        for (GoodsSkuDto goodsSkuDto : goodsSkuDtoList) {
            Integer colorSizeId = goodsSkuDto.getColorSizeId();
            Boolean isGoods = goodsSkuDto.getIsGoods();
            if (!hashOperations.hasKey(colorSizeId.toString() + isGoods.toString())) {
                // 如果redis中不存在该商品
                return false;
            }
            // 删除商品
            hashOperations.delete(colorSizeId.toString() + isGoods.toString());
        }
        return true;
    }


    /*
        根据购物车所有商品信息计算总价
    */
    public Map<String,Double> calculateTotalPrice(List<CartGoodsDto> cartGoodsList) {
        setDiscount();
        HashMap<String, Double> priceMap = new HashMap<>();
        // 优惠后的总价
        double totalPrice = 0;
        // 优惠前的总价
        double beforeTotalPrice = 0;
        for (CartGoodsDto cartGoodsDto : cartGoodsList) {
            // 单项商品总价
            double price = 0;
            // 如果勾选了，计算总价
            if (cartGoodsDto.getIsChosen() == Boolean.TRUE) {
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

    /*
        检查是否需要更新总价

    private double updateTotalPrice(AddGoodsDto addGoodsDto) {
        Integer isChosen = cartGoodsMapper.selectChosen(addGoodsDto);
        // 如果是已勾选的商品，则需要更新价格
        if (isChosen == 1) {
            // 更新价格并返回更新后的总价
            return calculateTotalPrice(addGoodsDto.getUserId());
        } else return 0;
    }

     */

}
