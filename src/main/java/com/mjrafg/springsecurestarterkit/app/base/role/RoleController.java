package com.mjrafg.springsecurestarterkit.app.base.role;

import com.mjrafg.springsecurestarterkit.base.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/roles")
public class RoleController extends BaseController<RoleEntity,String> {
    RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        super(roleService);
        this.roleService = roleService;
    }

}
