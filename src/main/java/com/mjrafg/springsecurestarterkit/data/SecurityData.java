package com.mjrafg.springsecurestarterkit.data;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityData {
    public static String getLoggedUserName() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return "anonymousUser";
        }
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
