package com.mjrafg.springsecurestarterkit.data;


import com.mjrafg.springsecurestarterkit.app.base.role.RoleEntity;
import com.mjrafg.springsecurestarterkit.app.base.role.RoleService;
import com.mjrafg.springsecurestarterkit.app.base.user.UserEntity;
import com.mjrafg.springsecurestarterkit.app.base.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    RoleService roleService;
    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        RoleEntity role1 = new RoleEntity("USER","user role");
        roleService.save(role1);

        RoleEntity role2 = new RoleEntity("MODERATOR","moderator role");
        roleService.save(role2);

        RoleEntity role3 = new RoleEntity("ADMIN","admin role");
        roleService.save(role3);

        UserEntity user1 = new UserEntity("mjrafg","jamil","mjrafg@yahoo.com", passwordEncoder.encode("123456"));
        user1.setRole(role2);
        userService.save(user1);
    }
}
