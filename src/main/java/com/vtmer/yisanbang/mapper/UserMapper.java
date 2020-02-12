package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    User selectByPrimaryKey(Integer id);

    List<User> selectAll();

    int updateByPrimaryKey(User record);

    // 新建微信用户信息
    int addUser(User user);

    // 根据微信用户OpenId查找用户信息
    User selectUserByOpenId(String openId);

    String selectOpenIdByUserId(Integer userId);
}