package com.bfs.hibernateprojectdemo.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.bfs.hibernateprojectdemo.domain.Role;
import com.bfs.hibernateprojectdemo.domain.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Integer id;
    private String username;
    private String email;
    private Role role;
    private List<String> permissions; // Store permissions directly as List<String>
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserDTO fromUser(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .permissions(user.getRole().getPermissions()) // Ensure this returns List<String>
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
