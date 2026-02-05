package com.huibai.eomp.service;

import com.huibai.eomp.entity.Departments;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
public interface IDepartmentsService extends IService<Departments> {
    /**
     * 获取部门树形列表
     * @return 嵌套结构的部门列表
     */
    List<Departments> getTreeList();

    /**
     * 禁用/启用部门
     * @param id 部门ID
     * @param status 状态值
     */
    void changeStatus(Integer id, String status);

    // 新增部门 (UC-05)
    void addDept(Departments dept);

    // 修改部门 (UC-05)
    void updateDept(Departments dept);
}
