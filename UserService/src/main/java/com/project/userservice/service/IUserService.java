package com.project.userservice.service;


import com.project.userservice.dto.ChangePasswordDTO;
import com.project.userservice.dto.UserDTO;
import com.project.userservice.exception.ValidationException;
import com.project.userservice.model.User;

import java.util.List;

public interface IUserService {
    User createUser(UserDTO userDTO);

    String login(String phoneNumber,String password) ;

    User updateUser(User user) throws ValidationException;
     User getUser(String token) throws ValidationException;
    User changePassword(User user,ChangePasswordDTO changePasswordDTO);
//    public List<User> getByRole(Long roleID);

    public List<User> findAll();

//    List<User> findAllUser(int roleId);
    void deleteUser(Long id, int isActive);
    List<User> findAllUserByRoleId(int roleId);
}
