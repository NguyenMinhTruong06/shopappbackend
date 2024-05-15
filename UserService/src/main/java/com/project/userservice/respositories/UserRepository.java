package com.project.userservice.respositories;



import com.project.userservice.model.User;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User>findByPhoneNumber(String phoneNumber);
}
