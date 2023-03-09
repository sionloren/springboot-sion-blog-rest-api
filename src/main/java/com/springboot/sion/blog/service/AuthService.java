package com.springboot.sion.blog.service;

import com.springboot.sion.blog.dto.LoginDto;
import com.springboot.sion.blog.dto.RegisterDto;

public interface AuthService {
    String login(LoginDto loginDto);
    String register(RegisterDto registerDto);
}
