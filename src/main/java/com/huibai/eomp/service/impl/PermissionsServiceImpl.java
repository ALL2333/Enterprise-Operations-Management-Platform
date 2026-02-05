package com.huibai.eomp.service.impl;

import com.huibai.eomp.entity.Permissions;
import com.huibai.eomp.mapper.PermissionsMapper;
import com.huibai.eomp.service.IPermissionsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
@Service
public class PermissionsServiceImpl extends ServiceImpl<PermissionsMapper, Permissions> implements IPermissionsService {
    @Override
    public List<Permissions> getTree() {
        // 1. 获取所有权限记录
        List<Permissions> all = this.list();
        // 2. 递归构建树
        return buildTree(all, 0);
    }

    private List<Permissions> buildTree(List<Permissions> list, int parentId) {
        return list.stream()
                .filter(p -> p.getParentId() == parentId)
                .peek(p -> p.setChildren(buildTree(list, p.getId())))
                .collect(Collectors.toList());
    }
}
