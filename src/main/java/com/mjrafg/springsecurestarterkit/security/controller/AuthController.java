package com.mjrafg.springsecurestarterkit.security.controller;

import com.mjrafg.springsecurestarterkit.app.base.role.RoleEntity;
import com.mjrafg.springsecurestarterkit.app.base.role.RoleService;
import com.mjrafg.springsecurestarterkit.app.base.user.UserEntity;
import com.mjrafg.springsecurestarterkit.app.base.user.UserService;
import com.mjrafg.springsecurestarterkit.payload.request.LoginRequest;
import com.mjrafg.springsecurestarterkit.payload.request.SignupRequest;
import com.mjrafg.springsecurestarterkit.payload.response.JwtResponse;
import com.mjrafg.springsecurestarterkit.payload.response.MainResponse;
import com.mjrafg.springsecurestarterkit.security.jwt.JwtUtils;
import com.mjrafg.springsecurestarterkit.security.services.UserDetailsImpl;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.Optional;

import static com.mjrafg.springsecurestarterkit.data.SecurityData.getLoggedUserName;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> profile() {
        String userName = getLoggedUserName();
        Optional<UserEntity> user = userService.findByUserName(userName);
        if (user.isPresent()) {
            return MainResponse.ok(new JwtResponse("", user.get()));
        } else {
            return MainResponse.error("Error: Can not find user!");
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        UserEntity user = userService.findByUserName(userDetails.getUsername()).orElseThrow();
        return MainResponse.ok(new JwtResponse(jwt, user));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userService.existsByUserName(signUpRequest.getUserName())) {
            return MainResponse.error("Error: Username is already taken!");
        }

        if (userService.existsByEmail(signUpRequest.getEmail())) {
            return MainResponse.error("Error: Email is already in use!");
        }

        // Create new user's account
        UserEntity user = new UserEntity(signUpRequest.getUserName(), signUpRequest.getName(), signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        String strRole = signUpRequest.getRole();

        RoleEntity userRole = roleService.findByName(strRole)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

        user.setRole(userRole);

        userService.save(user);
        return MainResponse.ok("User registered successfully!");
    }
}
