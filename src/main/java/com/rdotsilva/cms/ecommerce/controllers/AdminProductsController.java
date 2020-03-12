package com.rdotsilva.cms.ecommerce.controllers;

import com.rdotsilva.cms.ecommerce.models.ProductRepository;
import com.rdotsilva.cms.ecommerce.models.data.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminProductsController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String index(Model model) {
        List<Product> products = productRepository.findAll();

        model.addAttribute("products", products);

        return "admin/products/index";
    }
}
