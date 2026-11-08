package com.dormitory.repairbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dormitory.repairbackend.common.Result;
import com.dormitory.repairbackend.entity.SysUser;
import com.dormitory.repairbackend.mapper.UserMapper;
import com.dormitory.repairbackend.utils.JwtUtil;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserMapper userMapper;

    public AuthController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @PostMapping("/login")
    public Result<Map<String, String>> login(@RequestBody SysUser loginUser) {
        SysUser user = userMapper.selectOne(new QueryWrapper<SysUser>()
                .eq("username", loginUser.getUsername()));

        if (user == null || !user.getPassword().equals(loginUser.getPassword())) {
            return Result.error("用户名或密码错误");
        }

        // 生成 Token
        String token = JwtUtil.generateToken(user.getUsername(), user.getRole());
        Map<String, String> data = new HashMap<>();
        data.put("token", token);
        data.put("username", user.getUsername());
        return Result.success(data);
    }
}