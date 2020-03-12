package com.rdotsilva.cms.ecommerce.models;

import com.rdotsilva.cms.ecommerce.models.data.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
