package com.rdotsilva.cms.ecommerce.controllers;

import com.rdotsilva.cms.ecommerce.models.CategoryRepository;
import com.rdotsilva.cms.ecommerce.models.data.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoriesController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public String index(Model model) {
        List<Category> categories = categoryRepository.findAll();

        model.addAttribute("categories", categories);

        return "admin/categories/index";
    }

    // Use this example if you want a model attribute accessible to all methods with the name category
//    @ModelAttribute("category")
//    public Category getCategory() {
//        return new Category();
//    }

    @GetMapping("/add")
    public String add(Category category) {

        return "admin/categories/add";
    }
}
