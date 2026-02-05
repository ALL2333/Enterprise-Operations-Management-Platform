package com.huibai.eomp.controller;

import com.huibai.eomp.common.result.Result;
import com.huibai.eomp.service.IPermissionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author zhou
 * @since 2026-01-18
 */
@RestController
@RequestMapping("/system/permissions")
public class PermissionsController {

    @Autowired
    private IPermissionsService permissionsService;

    @GetMapping("/tree")
    public Result getTree() {
        return Result.success(permissionsService.getTree());
    }
}
