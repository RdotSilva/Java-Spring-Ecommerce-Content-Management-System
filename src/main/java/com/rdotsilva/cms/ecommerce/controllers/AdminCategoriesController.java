package com.rdotsilva.cms.ecommerce.controllers;

import com.rdotsilva.cms.ecommerce.models.CategoryRepository;
import com.rdotsilva.cms.ecommerce.models.data.Category;
import com.rdotsilva.cms.ecommerce.models.data.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoriesController {

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public String index(Model model) {
        List<Category> categories = categoryRepository.findAllByOrderBySortingAsc();

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

    @PostMapping("/add")
    public String add(@Valid Category category, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        // Check category for errors
        if (bindingResult.hasErrors()) {
            return "admin/categories/add";
        }

        // Display Category added if successful
        redirectAttributes.addFlashAttribute("message", "Category Added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        // Construct category slug
        String slug = category.getName().toLowerCase().replace(" ", "-");

        Category categoryExists = categoryRepository.findByName(category.getName());

        // Check if category exists, if so then display errors, otherwise set the new category slug and save repo
        if (categoryExists != null) {
            redirectAttributes.addFlashAttribute("message", "Category already exists, choose another slug");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("categoryInfo", category);
        } else {
            category.setSlug(slug);
            category.setSorting(100);

            categoryRepository.save(category);
        }

        // Redirect when done
        return "redirect:/admin/categories/add";
    }

    // Edit category, id is passed in from @PathVariable annotation below
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {

        Category category = categoryRepository.getOne(id);

        model.addAttribute("category", category);

        return "admin/categories/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid Category category, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        Category currentCategory = categoryRepository.getOne(category.getId());

        // Check category for errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryName", currentCategory.getName());
            return "admin/categories/edit";
        }

        // Display Page added if successful
        redirectAttributes.addFlashAttribute("message", "Category Edited");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        // Construct category slug
        String slug = category.getName().toLowerCase().replace(" ", "-");

        Category categoryExists = categoryRepository.findByName(category.getName());

        // Check if category exists, if so then display errors, otherwise set the new category slug and save repo
        if (categoryExists != null) {
            redirectAttributes.addFlashAttribute("message", "Category already exists, choose another slug");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        } else {
            category.setSlug(slug);

            categoryRepository.save(category);
        }

        // Redirect when done
        return "redirect:/admin/categories/edit/" + category.getId();
    }

    // Delete a category
    @GetMapping("/delete/{id}")
    public String edit(@PathVariable int id, RedirectAttributes redirectAttributes) {

        Category category = categoryRepository.getOne(id);
        categoryRepository.deleteById(id);

        redirectAttributes.addFlashAttribute("message", category.getName() + " category deleted!");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/admin/categories";
    }

    // Use @ResponseBody to return the response text "ok" to jquery.
    @PostMapping("/reorder")
    public @ResponseBody
    String reorder(@RequestParam("id[]") int[] id) {

        int count = 1;
        Category category;

        for (int categoryId : id) {
            category = categoryRepository.getOne(categoryId);
            category.setSorting(count);
            categoryRepository.save(category);
            count++;
        }

        return "ok";
    }
}
