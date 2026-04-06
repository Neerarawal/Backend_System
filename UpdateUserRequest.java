package com.zorvyn.finance.dto.request;

import com.zorvyn.finance.enums.Role;
import com.zorvyn.finance.enums.UserStatus;
import lombok.Data;

@Data
public class UpdateUserRequest {
    private Role role;
    private UserStatus status;
}
