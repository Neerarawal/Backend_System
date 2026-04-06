package com.zorvyn.finance.dto.response;

import com.zorvyn.finance.enums.Role;
import com.zorvyn.finance.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String username;
    private String email;
    private Role role;
    private UserStatus status;
    private LocalDateTime createdAt;
}
