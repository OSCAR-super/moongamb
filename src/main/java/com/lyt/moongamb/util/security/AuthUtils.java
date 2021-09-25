package com.lyt.moongamb.util.security;

import com.lyt.moongamb.security.entity.MyUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthUtils {
    public MyUserDetails getContextUserDetails(){
        return (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
