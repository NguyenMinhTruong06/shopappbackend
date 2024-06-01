package com.project.productservice.repositories;

import com.project.productservice.models.Option;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductOptionRepository extends JpaRepository<Option,Long > {
    List<Option> findByProductId(Long productId);

    Optional<Option> findById(Long id);
}
