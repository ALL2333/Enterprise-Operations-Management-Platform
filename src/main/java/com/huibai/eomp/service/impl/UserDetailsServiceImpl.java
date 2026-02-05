package com.huibai.eomp.service.impl;

import com.huibai.eomp.entity.Users;
import com.huibai.eomp.mapper.PermissionsMapper;
import com.huibai.eomp.mapper.UsersMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private PermissionsMapper permissionsMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. 查询数据库中的用户信息
        Users user = usersMapper.selectByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在: " + username);
        }

        // 2. 查询该用户拥有的权限标识列表 (如 ["system:user:delete", "system:user:edit"])
        List<String> perms = permissionsMapper.selectCodesByUserId(user.getId());

        // 3. 超级管理员特权处理
        if ("eomp_admin".equals(user.getUsername())) {
            perms.add("*:*:*");
        }

        // 4. 将权限字符串转为 Security 认领的 SimpleGrantedAuthority 对象
        List<SimpleGrantedAuthority> authorities = perms.stream()
                .filter(p -> p != null && !p.isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // 5. 返回 Security 原生的 User 对象
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPasswordHash(), // 数据库存的加密密码
                authorities
        );
    }
}