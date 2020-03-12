package com.rdotsilva.cms.ecommerce.models.data;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
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

    private String name;

    private String slug;

    private String description;

    private String image;

    private String price;

    private String quantity;

    // Use @Column annotation when the column ID is different than the field
    @Column(name = "category_id")
    private String categoryId;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
