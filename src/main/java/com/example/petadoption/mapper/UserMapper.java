package com.example.petadoption.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.petadoption.entity.User;

/**
 * 用户数据访问接口
 * 继承 MyBatis-Plus BaseMapper，自带增删改查、分页等方法，无需写 XML
 */
public interface UserMapper extends BaseMapper<User> {
}
