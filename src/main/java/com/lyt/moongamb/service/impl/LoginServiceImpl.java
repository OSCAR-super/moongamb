package com.lyt.moongamb.service.impl;

import com.lyt.moongamb.conctroller.login.request.UserLoginReq;
import com.lyt.moongamb.security.entity.MyUserDetails;
import com.lyt.moongamb.service.LoginService;
import com.lyt.moongamb.util.Res.RestResult;
import com.lyt.moongamb.util.redis.RedisService;
import com.lyt.moongamb.util.security.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtils jwtTokenUtils;

    @Autowired
    private RedisService redisService;

    @Override
    public RestResult userLogin(UserLoginReq userLoginReq) {
        Authentication authentication;
        try {
            // 进行身份验证,
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginReq.getUsername(), userLoginReq.getPassword()));
        } catch (Exception e) {

            //设置登入密码错误限制
            redisService.setUserLoginLimit(userLoginReq.getUsername());
            return new RestResult(0, e.getMessage(), null);
        }

        MyUserDetails loginUser = (MyUserDetails) authentication.getPrincipal();
        RestResult result = new RestResult(1, "登入成功", null);

        Collection<? extends GrantedAuthority> authorities = loginUser.getAuthorities();

        if (authorities.contains(new SimpleGrantedAuthority("user"))){
            result.setCode(1);
        }

        log.info("管理员:{} 已经登入。。。本次权限为:{}", loginUser.getUsername(), loginUser.getAuthorities().toString());

        //主动失效 设置黑名单 并关闭已存在socket
        if (redisService.userLogoutByServer(userLoginReq.getUsername()) == 0) {
            return null;
        }

        result.put("token", jwtTokenUtils.generateToken(loginUser, "user"));
        return result;
    }
}
