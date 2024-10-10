package com.vueblog.controller;

import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.vueblog.common.dto.LoginDto;
import com.vueblog.common.dto.RegisterDto;
import com.vueblog.common.lang.Result;
import com.vueblog.entity.User;
import com.vueblog.service.impl.UserServiceImpl;
import com.vueblog.utils.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
public class AccountController {
    @Autowired
    UserServiceImpl userService;
    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {
        User user = userService.getOne(new QueryWrapper<User>().eq("username", loginDto.getUsername()));
        Assert.notNull(user, "用户不存在");
        if (!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))) {
            return Result.fail("密码不正确");
        }
        String jwt = jwtUtils.generateToken(user.getId());
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-control-Expose-Headers", "Authorization");
        return Result.succ(MapUtil.builder().put("id", user.getId()).put("username", user.getUsername()).put("avatar", user.getAvatar()).put("email", user.getEmail()).map());
    }

    @RequiresAuthentication
    @GetMapping("/logout")
    public Result logout() {
        SecurityUtils.getSubject().logout();
        return Result.succ(null);
    }

    @PostMapping("/register")
    public Result register(@Valid @RequestBody RegisterDto registerDto, HttpServletResponse response) {
        // Check if username already exists
        User existingUser = userService.getOne(new QueryWrapper<User>().eq("username", registerDto.getUsername()));
        if (existingUser != null) {
            return Result.fail("用户名已存在.");
        }


        User newUser = new User();
        newUser.setUsername(registerDto.getUsername());

        newUser.setPassword(SecureUtil.md5(registerDto.getPassword()));


        userService.save(newUser);

        // Generate JWT token
        String jwt = jwtUtils.generateToken(newUser.getId());
        response.setHeader("Authorization", jwt);
        response.setHeader("Access-control-Expose-Headers", "Authorization");

        // Return user information
        return Result.succ(MapUtil.builder()
                .put("id", newUser.getId())
                .put("username", newUser.getUsername())
                .put("avatar", newUser.getAvatar())
                .put("email", newUser.getEmail())
                .map());
    }


    }
