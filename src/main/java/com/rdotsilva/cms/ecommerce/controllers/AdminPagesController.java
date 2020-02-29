package com.rdotsilva.cms.ecommerce.controllers;

import com.rdotsilva.cms.ecommerce.models.data.Page;
import com.rdotsilva.cms.ecommerce.models.data.PageRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
// This is the initial route for this controller. You must hit localhost/admin/pages to access methods in this controller
@RequestMapping("/admin/pages")
public class AdminPagesController {

    private PageRepository pageRepository;

    public AdminPagesController(PageRepository pageRepository) {
        this.pageRepository = pageRepository;
    }

    @GetMapping
    public String index(Model model) {

        // Get a list of pages using the page repo
        List<Page> pages = pageRepository.findAll();

        // Create a model for use in the view
        model.addAttribute("pages", pages);

        return "admin/pages/index";
    }

    @GetMapping("/add")
    public String add(Model model) {
        // Create a new model to pass into add.html
        // This is one way of doing it you can also use @ModelAttribute annotation as shown above
        model.addAttribute("page", new Page());
        return "admin/pages/add";
    }
}
