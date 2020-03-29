package com.rdotsilva.cms.ecommerce.controllers;

import com.rdotsilva.cms.ecommerce.models.CategoryRepository;
import com.rdotsilva.cms.ecommerce.models.ProductRepository;
import com.rdotsilva.cms.ecommerce.models.data.Category;
import com.rdotsilva.cms.ecommerce.models.data.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoriesController {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/{slug}")
    public String category(@PathVariable String slug,  Model model, @RequestParam(value="page", required = false) Integer p) {

        int perPage = 6;
        int page = (p != null) ? p : 0;

        // Use Pageable for pagination, page is the current page, perPage is the number of results per page.
        Pageable pageable = PageRequest.of(page, perPage);

        long count = 0;

        if (slug.equals("all")) {
            Page<Product> products = productRepository.findAll(pageable);

            count = productRepository.count();

            model.addAttribute("products", products);
        } else {
            
            Category category = categoryRepository.findBySlug(slug);

            if (category == null) {
                return "redirect:/";
            }

            int categoryId = category.getId();
            String categoryName = category.getName();
            List<Product> products = productRepository.findAllByCategoryId(Integer.toString(categoryId), pageable);

            count = productRepository.countByCategoryId(Integer.toString(categoryId));

            model.addAttribute("products", products);
            model.addAttribute("categoryName", categoryName);
        }

        // Calculate the total number of pages needed
        double pageCount = Math.ceil((double)count / (double) perPage);

        model.addAttribute("pageCount", (int)pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);

        return "products";
    }

}
