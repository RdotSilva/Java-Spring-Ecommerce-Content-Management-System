package com.rdotsilva.cms.ecommerce.models.data;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Data
public class Product {

    // Use @ID to mark as primary key
    // Use @GeneratedValue for auto increment ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Size(min = 2, message = "Name must be at least 2 characters long")
    private String name;

    private String slug;

    @Size(min = 5, message = "Description must be at least 5 characters long")
    private String description;

    private String image;

    @Pattern(regexp = "^[0-9]+([.][0-9]{1,2})?", message = "Expected format: 5, 5.99, 20, 20.99")
    private String price;

    @Size(min = 1, message = "Quantity must be greater than 0")
    private String quantity;

    // Use @Column annotation when the column ID is different than the field
    @Pattern(regexp = "^[0-9][0-9]*", message = "Please choose a category")
    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
