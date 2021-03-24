package com.hananoq.uaa.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hananoq.uaa.dao.UserDao;
import com.hananoq.uaa.domain.UserDto;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @author :花のQ
 * @since 2020/11/23 18:59
 **/
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Resource
    private UserDao userDao;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //将来连接数据库根据账号查询用户信息
        UserDto userDto = userDao.selectOne(new QueryWrapper<UserDto>().eq("username", username));
        if (userDto == null) {
            //如果用户查不到，返回null，由provider来抛出异常
            return null;
        }

        HashMap<String, Object> map = new HashMap<>();
        map.put("username", userDto.getUsername());
        String principal = JSON.toJSONString(map);

        return User
                .withUsername(principal)
                .password(passwordEncoder.encode(userDto.getPassword()))
                .authorities("test")
                .build();
    }
}
