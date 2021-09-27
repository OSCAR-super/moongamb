package com.lyt.moongamb.service;

import com.lyt.moongamb.controller.login.request.UserLoginReq;
import com.lyt.moongamb.util.Res.RestResult;

public interface LoginService {
    RestResult userLogin(UserLoginReq userLoginReq);
}
