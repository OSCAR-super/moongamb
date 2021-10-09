package com.lyt.moongamb.controller.login;

import com.lyt.moongamb.controller.login.request.TestReq;
import com.lyt.moongamb.controller.login.request.UserLoginReq;
import com.lyt.moongamb.controller.login.response.TestRes;
import com.lyt.moongamb.service.LoginService;
import com.lyt.moongamb.util.Res.RestResult;
import com.lyt.moongamb.util.Res.ResultUtils;
import com.lyt.moongamb.util.security.AuthUtils;
import com.lyt.moongamb.util.security.JwtTokenUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/login")
@PreAuthorize("permitAll()")
@RestController
@Slf4j
public class LoginController {
    @Autowired
    private JwtTokenUtils jwtTokenUtils;
    @Autowired
    private LoginService loginService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthUtils authUtils;

    @PostMapping( "/test")
    public RestResult test(@Validated @RequestBody TestReq testReq){
        TestRes testRes=new TestRes();
        if (testReq.getLoginCode().equals("ok")){
            testRes.setCode("ok");
        }else {
            testRes.setCode("no");
        }
        return ResultUtils.success(testRes);
    }

    @PostMapping("/adminLogin")
    public RestResult adminLogin(@Validated @RequestBody UserLoginReq userLoginReq) {
        System.out.println(passwordEncoder.encode(userLoginReq.getPassword()));
        //123qweqwe
        //$2a$10$aOst2pyWL/0xwXlufg3OT.mfX7MFvJmUX9iR6syXUBCf/GSm/wL4W
        //$2a$10$rDZkVR4Hv4H/HzFQuBlvEOcaUr9SxVXAftuU0YN0/lS0DANh4PKbm
        return loginService.userLogin(userLoginReq);
    }

    /*
    获取token 信息示例
     */
    @GetMapping("/testToken")
    public void getTokenTest(HttpServletRequest request) {
        String account = jwtTokenUtils.getAuthAccountFromRequest(request);
        log.info("当前登入用户为:{}", account);
        String test_account = authUtils.getContextUserDetails().getUsername();
        log.info("当前登入用户为:{}", test_account);
    }
}
