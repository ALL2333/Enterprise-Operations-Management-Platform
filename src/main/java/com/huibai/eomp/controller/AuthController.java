package com.huibai.eomp.controller;

import com.huibai.eomp.common.result.Result;
import com.huibai.eomp.dto.LoginDTO;
import com.huibai.eomp.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhouyihao
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginDTO loginDTO) {
        return Result.success(authService.login(loginDTO));
    }
}
