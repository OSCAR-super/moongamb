package com.lyt.moongamb.service;

import com.lyt.moongamb.entity.UserEntity;
import com.lyt.moongamb.entity.UserRoleEntity;

import java.util.List;

public interface UserService {
    UserEntity findUserByUsername(String username);

    List<UserRoleEntity> findUserRolesByUsername(String username);
}
