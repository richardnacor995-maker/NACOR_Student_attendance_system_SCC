package com.scc.model;

public record User(
        int id,
        String username,
        String password,
        String lastname,
        String courseSection,
        String email,
        String status
) {
}