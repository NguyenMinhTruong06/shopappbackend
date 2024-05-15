package com.project.userservice.service;


import com.project.userservice.dto.ChangePasswordDTO;
import com.project.userservice.dto.UserDTO;
import com.project.userservice.exception.ValidationException;
import com.project.userservice.model.User;

public interface IUserService {
    User createUser(UserDTO userDTO);

    String login(String phoneNumber,String password) ;

    User updateUser(User user) throws ValidationException;
     User getUser(String token) throws ValidationException;
    User changePassword(User user,ChangePasswordDTO changePasswordDTO);
}
