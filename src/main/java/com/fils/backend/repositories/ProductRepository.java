package com.fils.backend.repositories;

import com.fils.backend.domain.Product;
import com.fils.backend.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long productId);
    List<Product> findByNameContaining(String name);
    List<Product> findAllByTypeId(Long id);

    List<Product> findAllByUser(User user);
}
