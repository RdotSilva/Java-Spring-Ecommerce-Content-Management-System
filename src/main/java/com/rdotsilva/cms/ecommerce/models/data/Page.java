package com.rdotsilva.cms.ecommerce.models.data;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="pages")
@Data // lombok @Data used to auto create getters/setters for members below
public class Page {

    // Use @ID to mark as primary key
    // Use @GeneratedValue for auto increment ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String slug;
    private String content;
    private int sorting;
}
