package com.sms.dto;

public record LoginResponse(String token, String username, String role, String name, Long studentId) {
}
