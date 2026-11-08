package com.dormitory.repairbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dormitory.repairbackend.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<SysUser> {
}