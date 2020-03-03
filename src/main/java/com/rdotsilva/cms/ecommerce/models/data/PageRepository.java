package com.rdotsilva.cms.ecommerce.models.data;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Page is the page Entity, Integer is the int ID value
public interface PageRepository extends JpaRepository<Page, Integer> {

    // Custom query creation. Works by naming method in a very specific way.
    // Check Spring Data JPA documentation for more info.
    Page findBySlug(String slug);

    // Custom query example
    // Find slug outside of current page
//    @Query("SELECT p FROM Page p WHERE p.id != :id and p.slug = :slug")
//    Page findBySlugNotOnPage(int id, String slug);

    // This does same as custom query above just much neater
    Page findBySlugAndIdNot(String slug, int id);

    // Find all Pages sorted
    List<Page> findAllByOrderBySortingAsc();

}