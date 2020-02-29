package com.rdotsilva.cms.ecommerce.models.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Page is the page Entity, Integer is the int ID value
public interface PageRepository extends JpaRepository<Page, Integer> {

}