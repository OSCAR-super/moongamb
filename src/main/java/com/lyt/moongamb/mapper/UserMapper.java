package com.lyt.moongamb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lyt.moongamb.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper  extends BaseMapper<UserEntity> {
}
