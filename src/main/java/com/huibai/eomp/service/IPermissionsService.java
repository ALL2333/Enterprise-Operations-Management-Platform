package com.huibai.eomp.service;

import com.huibai.eomp.entity.Permissions;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 权限表 服务类
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
public interface IPermissionsService extends IService<Permissions> {
    List<Permissions> getTree();
}
