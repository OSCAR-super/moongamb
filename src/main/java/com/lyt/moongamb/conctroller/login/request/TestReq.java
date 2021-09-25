package com.lyt.moongamb.conctroller.login.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestReq {
    @NotNull
    private String loginCode;
}
