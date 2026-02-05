package com.huibai.eomp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huibai.eomp.common.exception.CustomException;
import com.huibai.eomp.common.utils.JwtUtils;
import com.huibai.eomp.dto.LoginDTO;
import com.huibai.eomp.entity.Users;
import com.huibai.eomp.mapper.UsersMapper;
import com.huibai.eomp.service.IAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhouyihao
 */
@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private UsersMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtil;

    @Override
    public Map<String, Object> login(LoginDTO loginDTO) {
        String newHash = passwordEncoder.encode("admin123456");
        System.out.println("最新的合法Hash值: " + newHash);
        // 1. 根据用户名查询数据库
        Users user = userMapper.selectOne(new QueryWrapper<Users>().eq("username", loginDTO.getUsername()));
        System.out.println("查询到的用户对象：" + user);
        if (user != null) {
            System.out.println("数据库里的加密密码是：" + user.getPasswordHash());
        }
        System.out.println("前端传来的原始密码是: [" + loginDTO.getPassword() + "]");
        // 2. 校验用户是否存在及密码是否匹配 (BCrypt校验)
        if (user == null || !passwordEncoder.matches(loginDTO.getPassword(), user.getPasswordHash())) {
            throw new CustomException("用户名或密码错误");
        }

        // 3. 校验账号状态
        if ("inactive".equals(user.getStatus())) {
            throw new CustomException("该账号已被禁用");
        }

        // 4. 生成 Token
        String token = jwtUtil.createToken(user.getId(), user.getUsername());

        // 5. 组装返回结果
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("username", user.getUsername());
        map.put("realName", user.getRealName());

        return map;
    }
}
