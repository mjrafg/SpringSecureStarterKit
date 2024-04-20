package com.mjrafg.springsecurestarterkit.app.base.role;

import com.mjrafg.springsecurestarterkit.base.service.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class RoleService  extends BaseServiceImpl<RoleEntity,String> {
    RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        super(roleRepository);
        this.roleRepository = roleRepository;
    }


    public Optional<RoleEntity> findByName(String name){
        return roleRepository.findByName(name);
    }
}
