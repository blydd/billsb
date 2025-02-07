package com.bgt.billsb.service.impl;
// 服务层实现类
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bgt.billsb.entity.User;
import com.bgt.billsb.mapper.UserMapper;
import com.bgt.billsb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> getAllUsers() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        return userMapper.selectList(queryWrapper);
    }

    @Override
    public void saveUser(User user) {
        userMapper.insert(user);
    }
}