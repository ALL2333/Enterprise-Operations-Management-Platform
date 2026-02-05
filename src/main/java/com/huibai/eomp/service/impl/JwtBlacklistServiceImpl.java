package com.huibai.eomp.service.impl;

import com.huibai.eomp.entity.JwtBlacklist;
import com.huibai.eomp.mapper.JwtBlacklistMapper;
import com.huibai.eomp.service.IJwtBlacklistService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * JWT令牌黑名单表 服务实现类
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
@Service
public class JwtBlacklistServiceImpl extends ServiceImpl<JwtBlacklistMapper, JwtBlacklist> implements IJwtBlacklistService {

}
