package com.project.userservice.service;


import com.project.userservice.component.JwtTokenUtil;
import com.project.userservice.dto.ChangePasswordDTO;
import com.project.userservice.dto.UserDTO;

import com.project.userservice.exception.ValidationException;
import com.project.userservice.model.Role;
import com.project.userservice.model.User;
import com.project.userservice.respositories.RoleRepository;
import com.project.userservice.respositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

@Autowired
    private PasswordEncoder passwordEncoder;

    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;
    private final Integer isActive=1;
    @Override
    public User createUser(UserDTO userDTO) {
        String phoneNumber = userDTO.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber)){
            throw new ValidationException(HttpStatus.CONFLICT ,"phone number đã tồn tại");
        }
        Role role= roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(()-> new ValidationException(HttpStatus.UNAUTHORIZED, "role not found"));
        if(role.getName().equals(Role.ADMIN)){
            throw new ValidationException(HttpStatus.UNAUTHORIZED, "You Cannot Register An Admin Account");

        }
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .phoneNumber(userDTO.getPhoneNumber())
                .password(userDTO.getPassword())
                .address(userDTO.getAddress())
                .dateOfBirth(userDTO.getDateOfBirth())
//                .fbAccountId(userDTO.getFacebookAccountId())
//               .ggAccountId(userDTO.getGoogleAccountId())
                .build();

        newUser.setRole(role);
        newUser.setIsActive(isActive);
//        if(userDTO.getFacebookAccountId()==0&& userDTO.getGoogleAccountId()==0){
            String password = userDTO.getPassword();
            String encodePassword = passwordEncoder.encode(password);
            newUser.setPassword(encodePassword);
//        }

        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) {
        Optional<User>optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        if(optionalUser.isEmpty()){
            throw  new ValidationException(HttpStatus.CONFLICT,"Invalid phonenumber / password");
        }

        if(optionalUser.get().getIsActive()!=1){
            throw new ValidationException(HttpStatus.CONFLICT,"Tài khoản của bạn đã bị khoá");

        }

        User existingUser = optionalUser.get();

//        if(existingUser.getFbAccountId()==0&&
//                existingUser.getGgAccountId()==0){
//            if(!passwordEncoder.matches(password,existingUser.getPassword())){
//                throw  new BadCredentialsException("wrong phone number or password");
//            }
//        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(phoneNumber,password,existingUser.getAuthorities());
        authenticationManager.authenticate(authenticationToken);
        return jwtTokenUtil.generateToken(existingUser);
        // trả vể token
    }
    @Override
    public User updateUser(User user) {

        return userRepository.save(user);
    }

    @Override
    public User getUser(String token)  {
        if(jwtTokenUtil.isTokenExpired(token)){
            throw new ValidationException(HttpStatus.CONFLICT,"token is expired");
        }
        String phoneNumber = jwtTokenUtil.extractPhoneNumber(token);
        Optional<User> user =userRepository.findByPhoneNumber(phoneNumber);
        if(user.isPresent()){
            return user.get();
        }
        else {
            throw new ValidationException(HttpStatus.CONFLICT,"User not found");
        }
    }
    @Override
    public User changePassword(User user, ChangePasswordDTO changePasswordDTO) {
        String pass= changePasswordDTO.getPassword();

        if (!passwordEncoder.matches(pass, user.getPassword())) {
            throw new ValidationException(HttpStatus.UNAUTHORIZED, "Current password is incorrect");
        }

        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmNewPassword())) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "New password and confirmation do not match");
        }

        String newEncodedPassword = passwordEncoder.encode(changePasswordDTO.getNewPassword());
         user.setPassword(newEncodedPassword);

         return userRepository.save(user);
    }

}
