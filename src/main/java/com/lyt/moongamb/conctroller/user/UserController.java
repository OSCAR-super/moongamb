package com.lyt.moongamb.conctroller.user;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;


@RequestMapping("/user")
@PreAuthorize("hasAuthority('user')")
public class UserController {

}
