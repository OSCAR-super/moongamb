package com.lyt.moongamb.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "tb_userRole")
public class UserRoleEntity {
    @Id
    private String id;
    private String username;
    private String role;
    @Version
    private Integer version;
}
