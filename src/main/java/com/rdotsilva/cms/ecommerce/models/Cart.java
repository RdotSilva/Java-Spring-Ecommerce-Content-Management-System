package com.rdotsilva.cms.ecommerce.models;

import lombok.Data;

@Data
public class Cart {

    private int id;
    private String name;
    private String price;
    private int quantity;
    private String image;
}
