package com.project.productservice.repositories;

import com.project.productservice.models.Order;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserIdAndActiveTrue(Long userId);
    List<Order> findOrderByActiveTrue();


}
