package com.project.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDate;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    @JsonProperty("fullname")
    private String fullName;
    @JsonProperty("phone_number")
    private String phoneNumber;
    private String address;


    @JsonProperty("retype_password")
    private String retypePassword;
    @JsonProperty("password")
    private String password;

    @JsonProperty("date_of_birth")
    private LocalDate dateOfBirth;
//    @JsonProperty("fb_account_id")
//    private Integer facebookAccountId;
//    @JsonProperty("gg_account_id")
//    private Integer googleAccountId;
    @JsonProperty("is_active")
    private Integer isActive;
    @JsonProperty("role_id")
    private Long roleId;
}
