package com.rdotsilva.cms.ecommerce.controllers;

import com.rdotsilva.cms.ecommerce.models.CategoryRepository;
import com.rdotsilva.cms.ecommerce.models.ProductRepository;
import com.rdotsilva.cms.ecommerce.models.data.Category;
import com.rdotsilva.cms.ecommerce.models.data.Product;
import com.rdotsilva.cms.ecommerce.models.data.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminProductsController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public String index(Model model, @RequestParam(value="page", required = false) Integer p) {

        int perPage = 6;
        int page = (p != null) ? p : 0;

        // Use Pageable for pagination, page is the current page, perPage is the number of results per page.
        Pageable pageable = PageRequest.of(page, perPage);

        Page<Product> products = productRepository.findAll(pageable);

        List<Category> categories = categoryRepository.findAll();

        // Create a HasMap with each category and id
        HashMap<Integer, String> cats = new HashMap<>();
        for (Category cat : categories) {
            cats.put(cat.getId(), cat.getName());
        }

        model.addAttribute("products", products);
        model.addAttribute("cats", cats);

        // Calculate the total number of pages needed
        long count = productRepository.count();
        double pageCount = Math.ceil((double)count / (double) perPage);

        model.addAttribute("pageCount", (int)pageCount);
        model.addAttribute("perPage", perPage);
        model.addAttribute("count", count);
        model.addAttribute("page", page);

        return "admin/products/index";
    }

    @GetMapping("/add")
    public String add(Product product, Model model) {

        // Get all categories from the repo and then send them to the view as a model named "categories"
        // This allows us to use "categories" model in our html files with Thymeleaf
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);

        return "admin/products/add";
    }

    @PostMapping("/add")
    public String add(@Valid Product product, BindingResult bindingResult, MultipartFile file, RedirectAttributes redirectAttributes, Model model) throws IOException {

        List<Category> categories = categoryRepository.findAll();

        Product currentProduct = productRepository.getOne(product.getId());

        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categories);
            return "admin/products/add";
        }

        // Check image file to make sure it is valid
        boolean fileOK = false;
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/media/" + filename);

        // Check for JPG or PNG only
        if (filename.endsWith("jpg") || filename.endsWith("png")) {
            fileOK = true;
        }


        // Display Product added if successful
        redirectAttributes.addFlashAttribute("message", "Product Added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        // Construct product slug
        String slug = product.getName().toLowerCase().replace(" ", "-");

        Product productExists = productRepository.findBySlug(slug);

        // If image file is not OK send error message
        if (!fileOK ) {
            redirectAttributes.addFlashAttribute("message", "Image must be JPG or PNG");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            // Make form sticky (stays if there is an error)
            redirectAttributes.addFlashAttribute("product", product);
        }
        // Check if product exists, if so then display errors, otherwise set the new product slug, new product image, and save repo
        else if (productExists != null) {
            redirectAttributes.addFlashAttribute("message", "Product already exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            // Make form sticky (stays if there is an error)
            redirectAttributes.addFlashAttribute("product", product);
        } else {
            product.setSlug(slug);
            product.setImage(filename);

            productRepository.save(product);
            Files.write(path, bytes);
        }

        // Redirect when done
        return "redirect:/admin/products/add";
    }

    // Edit product, id is passed in from @PathVariable annotation below
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {

        Product product = productRepository.getOne(id);
        List<Category> categories = categoryRepository.findAll();

        model.addAttribute("product", product);
        model.addAttribute("categories", categories);

        return "admin/products/edit";
    }

    @PostMapping("/edit")
    public String edit(@Valid Product product, BindingResult bindingResult, MultipartFile file, RedirectAttributes redirectAttributes, Model model) throws IOException {

        Product currentProduct = productRepository.getOne(product.getId());
        List<Category> categories = categoryRepository.findAll();

        if (bindingResult.hasErrors()) {
            model.addAttribute("product", currentProduct.getName());
            model.addAttribute("categories", categories);
            return "admin/products/edit";
        }

        // Check image file to make sure it is valid
        boolean fileOK = false;
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static/media/" + filename);

        if (!file.isEmpty()) {
            // Check for JPG or PNG only
            if (filename.endsWith("jpg") || filename.endsWith("png")) {
                fileOK = true;
            }
        } else {
            fileOK = true;
        }


        // Display Product added if successful
        redirectAttributes.addFlashAttribute("message", "Product Edited");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        // Construct product slug
        String slug = product.getName().toLowerCase().replace(" ", "-");

        Product productExists = productRepository.findBySlugAndIdNot(slug, product.getId());

        // If image file is not OK send error message
        if (!fileOK ) {
            redirectAttributes.addFlashAttribute("message", "Image must be JPG or PNG");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            // Make form sticky (stays if there is an error)
            redirectAttributes.addFlashAttribute("product", product);
        }
        // Check if product exists, if so then display errors, otherwise set the new product slug, new product image, and save repo
        else if (productExists != null) {
            redirectAttributes.addFlashAttribute("message", "Product already exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            // Make form sticky (stays if there is an error)
            redirectAttributes.addFlashAttribute("product", product);
        } else {

            product.setSlug(slug);

            // Check if image exists and replace old image with new image
            if (!file.isEmpty()) {
                Path path2 = Paths.get("src/main/resources/static/media/" + currentProduct.getImage());
                Files.delete(path2);
                product.setImage(filename);
                Files.write(path, bytes);
            } else {
                product.setImage(currentProduct.getImage());
            }

            productRepository.save(product);
        }

        // Redirect when done
        return "redirect:/admin/products/edit/" + product.getId();
    }

    // Delete a product
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) throws IOException {

        Product product = productRepository.getOne(id);

        Product currentProduct = productRepository.getOne(product.getId());

        // Delete image from media folder
        Path path2 = Paths.get("src/main/resources/static/media/" + currentProduct.getImage());
        Files.delete(path2);


        productRepository.deleteById(id);

        redirectAttributes.addFlashAttribute("message", product.getName() + " product deleted!");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/admin/products";
    }
}
