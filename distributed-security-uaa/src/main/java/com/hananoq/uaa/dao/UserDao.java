package com.hananoq.uaa.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hananoq.uaa.domain.UserDto;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author :花のQ
 * @since 2020/11/23 18:47
 **/
@Mapper
public interface UserDao extends BaseMapper<UserDto> {
}
