package com.rcaengine.dto;

public record LoginResponse(

        String token,

        String username,

        String role

) {
}