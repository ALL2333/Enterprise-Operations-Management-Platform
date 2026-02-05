package com.huibai.eomp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huibai.eomp.entity.Departments;
import com.huibai.eomp.entity.Users;
import com.huibai.eomp.mapper.DepartmentsMapper;
import com.huibai.eomp.service.IDepartmentsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huibai.eomp.service.IUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
@Service
public class DepartmentsServiceImpl extends ServiceImpl<DepartmentsMapper, Departments> implements IDepartmentsService {

    @Autowired
    @Lazy
    private IUsersService usersService;
    @Override
    public List<Departments> getTreeList() {
        // 1. 获取所有部门数据
        List<Departments> allNodes = this.list();

        // 2. 获取所有用户数据，用于匹配姓名
        List<Users> allUsers = usersService.list();

        // 3. 将用户 ID 和姓名转为 Map 方便查询
        Map<Integer, String> userMap = allUsers.stream()
                .collect(Collectors.toMap(Users::getId, Users::getRealName, (v1, v2) -> v1));

        // 4. 组装树形结构并填充 managerName
        return allNodes.stream()
                .filter(m -> m.getParentId() == null || m.getParentId() == 0)
                .map(m -> {
                    // 填充当前节点的负责人姓名
                    if (m.getManagerId() != null) {
                        m.setManagerName(userMap.get(m.getManagerId()));
                    }
                    m.setChildren(getChildren(m, allNodes, userMap)); // 递归时也传入 map
                    return m;
                })
                .collect(Collectors.toList());
    }

    // 递归函数也要相应修改以支持姓名填充
    private List<Departments> getChildren(Departments root, List<Departments> all, Map<Integer, String> userMap) {
        return all.stream()
                .filter(m -> root.getId().equals(m.getParentId()))
                .map(m -> {
                    if (m.getManagerId() != null) {
                        m.setManagerName(userMap.get(m.getManagerId()));
                    }
                    m.setChildren(getChildren(m, all, userMap));
                    return m;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void changeStatus(Integer id, String status) {
        Departments dept = new Departments();
        dept.setId(id);
        dept.setStatus(status);
        this.updateById(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addDept(Departments dept) {
        // 1. 简单校验：同级目录下不允许重名
        long count = this.count(new LambdaQueryWrapper<Departments>()
                .eq(Departments::getParentId, dept.getParentId())
                .eq(Departments::getName, dept.getName()));
        if (count > 0) {
            throw new RuntimeException("该目录下已存在同名部门");
        }

        // 2. 初始化状态和时间 (也可以用 MyBatis-Plus 自动填充)
        dept.setStatus("active");
        dept.setCreatedAt(LocalDateTime.now());
        dept.setUpdatedAt(LocalDateTime.now());

        this.save(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDept(Departments dept) {
        // 1. 校验：不能把自己的父级设置为自己（防止陷入死循环）
        if (dept.getId().equals(dept.getParentId())) {
            throw new RuntimeException("上级部门不能选择自身");
        }

        // 2. 更新修改时间
        dept.setUpdatedAt(LocalDateTime.now());

        this.updateById(dept);
    }
}
