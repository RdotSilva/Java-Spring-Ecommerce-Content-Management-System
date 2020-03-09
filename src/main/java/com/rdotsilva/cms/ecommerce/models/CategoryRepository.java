package com.rdotsilva.cms.ecommerce.models;

import com.rdotsilva.cms.ecommerce.models.data.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
