package com.bfs.hibernateprojectdemo.domain;

import java.util.List;

public enum Role {
    USER,
    ADMIN;

    public List<String> getPermissions() {
        switch (this) {
            case ADMIN:
                return List.of("READ", "WRITE", "UPDATE", "DELETE");
            case USER:
                return List.of("READ");
            default:
                return List.of();
        }
    }
}
