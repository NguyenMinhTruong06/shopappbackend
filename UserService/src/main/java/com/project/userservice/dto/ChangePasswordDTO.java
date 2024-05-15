package com.project.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChangePasswordDTO {
    @JsonProperty("password")
    private String password;
    @JsonProperty("new_password")
    private String newPassword;
    @JsonProperty("confirm_new_password")
    private String confirmNewPassword;
}
