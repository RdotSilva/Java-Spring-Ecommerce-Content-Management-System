package com.rdotsilva.cms.ecommerce.controllers;

import com.rdotsilva.cms.ecommerce.models.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/add/{id}")
    public String add(@PathVariable int id, HttpSession session, Model model) {

        return null;

    }

}
