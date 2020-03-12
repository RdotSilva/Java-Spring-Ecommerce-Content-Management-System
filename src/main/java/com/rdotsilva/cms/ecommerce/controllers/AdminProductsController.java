package com.rdotsilva.cms.ecommerce.controllers;

import com.rdotsilva.cms.ecommerce.models.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/products")
public class AdminProductsController {

    @Autowired
    private ProductRepository productRepository;
}
