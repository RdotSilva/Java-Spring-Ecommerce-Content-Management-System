package com.rdotsilva.cms.ecommerce.controllers;

import com.rdotsilva.cms.ecommerce.models.data.Page;
import com.rdotsilva.cms.ecommerce.models.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
// This is the initial route for this controller. You must hit localhost/admin/pages to access methods in this controller
@RequestMapping("/admin/pages")
public class AdminPagesController {

    @Autowired
    private PageRepository pageRepository;

    // By using @Autowired above, we can comment out this code. Instead the pageRepository will be automatically
    // injected into code as needed.
//    public AdminPagesController(PageRepository pageRepository) {
//        this.pageRepository = pageRepository;
//    }

    @GetMapping
    public String index(Model model) {

        // Get a list of pages (sorted asc order) using the page repo
        List<Page> pages = pageRepository.findAllByOrderBySortingAsc();

        // Create a model for use in the view
        model.addAttribute("pages", pages);

        return "admin/pages/index";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute Page page) {

        // Create a new model to pass into add.html
        // This is one way of doing it you can also use @ModelAttribute annotation as shown above
//        model.addAttribute("page", new Page());
        return "admin/pages/add";
    }

    @PostMapping("/add")
    public String add(@Valid Page page, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        Page pageCurrent = pageRepository.getOne(page.getId());

        // Check page for errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", pageCurrent.getTitle());
            return "admin/pages/add";
        }

        // Display Page added if successful
        redirectAttributes.addFlashAttribute("message", "Page Added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        // Construct page slug
        String slug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ", "-") : page.getSlug().toLowerCase().replace(" ", "-");

        Page slugExists = pageRepository.findBySlug(slug);

        // Check if slug exists, if so then display errors, otherwise set the new page slug and save repo
        if (slugExists != null) {
            redirectAttributes.addFlashAttribute("message", "Slug already exists, choose another slug");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("page", page);
        } else {
            page.setSlug(slug);
            page.setSorting(100);

            pageRepository.save(page);
        }

        // Redirect when done
        return "redirect:/admin/pages/add";
    }

    // Edit page, id is passed in from @PathVariable annotation below
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {

        Page page = pageRepository.getOne(id);

        model.addAttribute("page", page);

        return "admin/pages/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid Page page, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        // Check page for errors
        if (bindingResult.hasErrors()) {
            return "admin/pages/edit";
        }

        // Display Page added if successful
        redirectAttributes.addFlashAttribute("message", "Page Edited");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        // Construct page slug
        String slug = page.getSlug() == "" ? page.getTitle().toLowerCase().replace(" ", "-") : page.getSlug().toLowerCase().replace(" ", "-");

        Page slugExists = pageRepository.findBySlugAndIdNot(slug, page.getId());

        // Check if slug exists (OUTSIDE OF THE CURRENT PAGE), if so then display errors, otherwise set the new page slug and save repo
        if (slugExists != null) {
            redirectAttributes.addFlashAttribute("message", "Slug already exists, choose another slug");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("page", page);
        } else {
            page.setSlug(slug);

            pageRepository.save(page);
        }

        // Redirect when done
        return "redirect:/admin/pages/edit/" + page.getId();
    }

    // Delete a page
    @GetMapping("/delete/{id}")
    public String edit(@PathVariable int id, RedirectAttributes redirectAttributes) {

        Page page = pageRepository.getOne(id);
        pageRepository.deleteById(id);

        redirectAttributes.addFlashAttribute("message", page.getTitle() + " page deleted!");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/admin/pages";
    }

    // Use @ResponseBody to return the response text "ok" to jquery.
    @PostMapping("/reorder")
    public @ResponseBody String reorder(@RequestParam("id[]") int[] id) {

        int count = 1;
        Page page;

        for (int pageId : id) {
            page = pageRepository.getOne(pageId);
            page.setSorting(count);
            pageRepository.save(page);
            count++;
        }

        return "ok";
    }
}
