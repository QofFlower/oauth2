package com.hananoq.uaa;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hananoq.uaa.dao.UserDao;
import com.hananoq.uaa.domain.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class DistributedSecurityUaaApplicationTests {

    @Resource
    private UserDao userDao;

    @Test
    void contextLoads() {

        UserDto userDto = userDao.selectOne(new QueryWrapper<UserDto>().eq("username", "mengbiyousi"));
        System.out.println("userDto = " + userDto);

    }

}
