package com.rdotsilva.cms.ecommerce.models.data;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name="pages")
@Data // lombok @Data used to auto create getters/setters for members below
public class Page {

    // Use @ID to mark as primary key
    // Use @GeneratedValue for auto increment ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // @Size is used for validation
    @Size(min = 2, message = "Title must be at least 2 characters long")
    private String title;
    private String slug;

    @Size(min = 5, message = "Content must be at least 5 characters long")
    private String content;
    private int sorting;
}
