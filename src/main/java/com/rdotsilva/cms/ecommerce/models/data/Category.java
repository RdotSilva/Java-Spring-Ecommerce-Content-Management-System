package com.rdotsilva.cms.ecommerce.models.data;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name="categories")
@Data
public class Category {

    // Use @ID to mark as primary key
    // Use @GeneratedValue for auto increment ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Use size for validation
    @Size(min=2, message = "Name must be at least 2 characters long")
    private String name;

    private String slug;

    private int sorting;
}
