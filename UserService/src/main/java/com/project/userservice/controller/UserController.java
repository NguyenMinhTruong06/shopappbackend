package com.project.userservice.controller;


import com.project.userservice.dto.ChangePasswordDTO;
import com.project.userservice.dto.UserDTO;
import com.project.userservice.dto.UserLoginDTO;
import com.project.userservice.dto.error.ErrorResponseDto;
import com.project.userservice.dto.response.AuthResponse;
import com.project.userservice.exception.ValidationException;
import com.project.userservice.model.User;
import com.project.userservice.response.UserResponse;
import com.project.userservice.respositories.UserRepository;
import com.project.userservice.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;



    @PostMapping("/register")
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDTO userDTO, BindingResult result)  {

            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors().stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);

            }
            if (!userDTO.getPassword().equals(userDTO.getRetypePassword())) {
                return ResponseEntity.badRequest().body("Password does not match");
            }
            User user = userService.createUser(userDTO);
            return ResponseEntity.ok(user);

    }

    @PreAuthorize("permitAll()")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginDTO userLoginDTO) {

            String token = userService.login(userLoginDTO.getPhoneNumber(), userLoginDTO.getPassword());
            return ResponseEntity.ok(new AuthResponse(token));

    }
    @PutMapping("/update")
    public ResponseEntity<?> update(@Valid @RequestBody UserDTO userUpdateDTO,
                                    @RequestHeader("Authorization") String authorizationHeader){
        String token = authorizationHeader.substring(7);
        User user = userService.getUser(token);
        user.setFullName(userUpdateDTO.getFullName());
        user.setAddress(userUpdateDTO.getAddress());
        user.setDateOfBirth(userUpdateDTO.getDateOfBirth());
        user.setUpdatedAt(LocalDateTime.now());

        User updateUser = userService.updateUser(user);
        return ResponseEntity.ok().body(updateUser);

    }
    @GetMapping("/getuser")
    public ResponseEntity<?> getUser(@RequestHeader("Authorization") String authorizationHeader){
        String extraToken = authorizationHeader.substring(7);
        User user = userService.getUser(extraToken);
        return ResponseEntity.ok(UserResponse.fromUser(user));

    }
    @PostMapping("/changepassword")
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String authorizationHeader,
                                            @Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        User user = userService.getUser(token);
        User userChangePass= userService.changePassword(user, changePasswordDTO);

        return ResponseEntity.ok().body(userChangePass);
    }

}
