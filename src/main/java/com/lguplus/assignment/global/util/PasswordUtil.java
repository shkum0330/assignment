package com.lguplus.assignment.global.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtil {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // 비밀번호 암호화
    public static String encode(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    // 비밀번호 검증
    public static boolean matches(String rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }
}
