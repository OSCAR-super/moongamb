package com.lyt.moongamb.conctroller.login.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginReq {
    @NotBlank(message = "用户名不能为空")
    @Size(max = 20)
    private String username;
    @NotBlank(message = "密码不能为空")
    @Size(max = 20)
    private String password;
}
