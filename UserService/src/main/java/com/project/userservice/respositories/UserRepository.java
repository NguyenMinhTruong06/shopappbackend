package com.project.userservice.respositories;



import com.project.userservice.model.User;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User>findByPhoneNumber(String phoneNumber);

//    List<User> findByRole(Long roleId);

    List<User> getAllBy();

    List<User> findByRoleId(int roleId);

//    List<User>findByRole(int roleId);
}
