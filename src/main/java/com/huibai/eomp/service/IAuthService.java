package com.huibai.eomp.service;

import com.huibai.eomp.dto.LoginDTO;

import java.util.Map;

public interface IAuthService {
    /**
     * 用户登录
     * @param loginDTO 登录参数
     * @return 包含 Token 和用户信息的 Map
     */
    Map<String, Object> login(LoginDTO loginDTO);
}
