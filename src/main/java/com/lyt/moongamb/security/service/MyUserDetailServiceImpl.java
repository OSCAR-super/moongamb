package com.lyt.moongamb.security.service;

import com.lyt.moongamb.entity.UserEntity;
import com.lyt.moongamb.entity.UserRoleEntity;
import com.lyt.moongamb.security.entity.MyUserDetails;
import com.lyt.moongamb.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MyUserDetailServiceImpl implements UserDetailsService {


    @Autowired
    private UserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userService.findUserByUsername(username);
        if(userEntity ==null){
            throw new RuntimeException(username+"账号不存在");
        }
        List<UserRoleEntity> userRoleEntities = userService.findUserRolesByUsername(username);
        List<GrantedAuthority> authoritys = new ArrayList<>();
        for (UserRoleEntity userRoleEntity : userRoleEntities){
            SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(userRoleEntity.getRole());
            authoritys.add(simpleGrantedAuthority);
        }

        return new MyUserDetails(userEntity.getUsername(), userEntity.getPassword(),authoritys);
    }
}
