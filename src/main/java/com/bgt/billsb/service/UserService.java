package com.bgt.billsb.service;// 服务层接口
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bgt.billsb.entity.User;
import com.bgt.billsb.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    void saveUser(User user);
}
