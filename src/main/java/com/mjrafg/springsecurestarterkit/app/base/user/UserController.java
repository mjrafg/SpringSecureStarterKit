package com.mjrafg.springsecurestarterkit.app.base.user;

import com.mjrafg.springsecurestarterkit.base.controller.BaseController;
import com.mjrafg.springsecurestarterkit.payload.response.MainResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController<UserEntity, String> {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        super(userService);
        this.userService = userService;
    }
    @PutMapping("/profile/{userId}/{fileId}")
    public ResponseEntity<?> updateProfileUser(@PathVariable String userId, @PathVariable String fileId) {
        Optional<UserEntity> updatedUser = userService.updateProfileImage(userId, fileId);
        if (updatedUser.isPresent()) {
            return MainResponse.ok(updatedUser.get());
        } else {
            return MainResponse.error("User not found");
        }
    }

    @DeleteMapping("/profile/{userId}/{fileId}")
    public ResponseEntity<?> deleteProfileUser(@PathVariable String userId, @PathVariable String fileId) {
        userService.deleteProfileImage(userId, fileId);
        return MainResponse.ok();
    }
    @GetMapping("/check/username/{searchUserName}/{currentUserName}")
    public ResponseEntity<?> checkUserName(@PathVariable String searchUserName, @PathVariable String currentUserName) {
        boolean isExist = userService.checkUserName(searchUserName,currentUserName);
        return MainResponse.ok(isExist);
    }
}
