package com.fils.backend.repositories;

import com.fils.backend.domain.Product;
import com.fils.backend.domain.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductTypeRepository extends JpaRepository<Long, ProductType> {
    
}
