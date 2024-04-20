package com.mjrafg.springsecurestarterkit.payload.response;

import com.mjrafg.springsecurestarterkit.config.app.AppConfig;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class MainResponse {


    private int code = AppConfig.OK_CODE;
    private Object data = null;
    private String message = null;
    private String messageColor = null;
    private final Map<String, Object> response = new HashMap<>();

    private void build() {
        response.put("status", code);
        if (data != null)
            response.put("data", data);
        if (message != null)
            response.put("message", message);
        if (messageColor != null)
            response.put("messageColor", messageColor);
    }

    public static ResponseEntity<?> ok() {
        MainResponse mainResponse = new MainResponse();
        mainResponse.build();
        return ResponseEntity.ok(mainResponse.response);
    }

    public static ResponseEntity<?> ok(String message) {
        MainResponse mainResponse = new MainResponse();
        mainResponse.messageColor = AppConfig.OK_MSG_CLR;
        mainResponse.message = message;
        mainResponse.build();
        return ResponseEntity.ok(mainResponse.response);
    }

    public static ResponseEntity<?> ok(Object data) {
        MainResponse mainResponse = new MainResponse();
        mainResponse.data = data;
        mainResponse.build();
        return ResponseEntity.ok(mainResponse.response);
    }

    public static ResponseEntity<?> ok(Object data, String message) {
        MainResponse mainResponse = new MainResponse();
        mainResponse.data = data;
        mainResponse.message = message;
        mainResponse.messageColor = AppConfig.OK_MSG_CLR;
        mainResponse.build();
        return ResponseEntity.ok(mainResponse.response);
    }

    public static ResponseEntity<?> ok(Object data, String message, String messageColor) {
        MainResponse mainResponse = new MainResponse();
        mainResponse.data = data;
        mainResponse.message = message;
        mainResponse.messageColor = messageColor;
        mainResponse.build();
        return ResponseEntity.ok(mainResponse.response);
    }

    public static ResponseEntity<?> error(String message) {
        MainResponse mainResponse = new MainResponse();
        mainResponse.code = AppConfig.ERROR_CODE;
        mainResponse.message = message;
        mainResponse.messageColor = AppConfig.ERR_MSG_CLR;
        mainResponse.build();
        return ResponseEntity.ok(mainResponse.response);
    }

    public static ResponseEntity<?> error() {
        MainResponse mainResponse = new MainResponse();
        mainResponse.code = AppConfig.ERROR_CODE;
        mainResponse.build();
        return ResponseEntity.ok(mainResponse.response);
    }

    public static ResponseEntity<?> error(Object data) {
        MainResponse mainResponse = new MainResponse();
        mainResponse.code = AppConfig.ERROR_CODE;
        mainResponse.data = data;
        mainResponse.build();
        return ResponseEntity.ok(mainResponse.response);
    }

    public static ResponseEntity<?> error(Object data, String message) {
        MainResponse mainResponse = new MainResponse();
        mainResponse.code = AppConfig.ERROR_CODE;
        mainResponse.data = data;
        mainResponse.message = message;
        mainResponse.messageColor = AppConfig.ERR_MSG_CLR;
        mainResponse.build();
        return ResponseEntity.ok(mainResponse.response);
    }

    public static ResponseEntity<?> error(Object data, String message, String messageColor) {
        MainResponse mainResponse = new MainResponse();
        mainResponse.code = AppConfig.ERROR_CODE;
        mainResponse.data = data;
        mainResponse.message = message;
        mainResponse.messageColor = messageColor;
        mainResponse.build();
        return ResponseEntity.ok(mainResponse.response);
    }

    public static ResponseEntity<?> unauth() {
        MainResponse mainResponse = new MainResponse();
        mainResponse.code = AppConfig.UN_AUTH_CODE;
        mainResponse.message = AppConfig.UN_AUTH_MSG;
        mainResponse.messageColor = AppConfig.ERR_MSG_CLR;
        mainResponse.build();
        return ResponseEntity.ok(mainResponse.response);
    }

}
