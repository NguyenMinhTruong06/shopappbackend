package com.project.userservice.response;

import com.project.userservice.model.BaseEntity;
import com.project.userservice.model.User;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse extends BaseEntity {
        private String fullName;
        private String phoneNumber;
        private String address;
        private LocalDate dateOfBirth;
        private int isActive;
        private Long id;
        public static UserResponse fromUser(User user){

            UserResponse userResponse = UserResponse.builder()
                        .id(user.getId())
                        .phoneNumber(user.getPhoneNumber())
                        .fullName(user.getFullName())
                        .address(user.getAddress())
                        .dateOfBirth(user.getDateOfBirth())
                        .isActive(user.getIsActive())
                        .build();
            userResponse.setCreatedAt(user.getCreatedAt());
            userResponse.setUpdatedAt(user.getUpdatedAt());
            return userResponse;
        }
}
