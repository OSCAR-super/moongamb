package com.lyt.moongamb.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lyt.moongamb.entity.UserEntity;
import com.lyt.moongamb.entity.UserRoleEntity;
import com.lyt.moongamb.mapper.UserMapper;
import com.lyt.moongamb.mapper.UserRoleMapper;
import com.lyt.moongamb.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    @Override
    public UserEntity findUserByUsername(String username) {
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        return userMapper.selectOne(wrapper);
    }

    @Override
    public List<UserRoleEntity> findUserRolesByUsername(String username) {
        QueryWrapper<UserRoleEntity>wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        return userRoleMapper.selectList(wrapper);
    }
}
