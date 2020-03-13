package com.rdotsilva.cms.ecommerce.controllers;

import com.rdotsilva.cms.ecommerce.models.CategoryRepository;
import com.rdotsilva.cms.ecommerce.models.ProductRepository;
import com.rdotsilva.cms.ecommerce.models.data.Category;
import com.rdotsilva.cms.ecommerce.models.data.Product;
import com.rdotsilva.cms.ecommerce.models.data.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminProductsController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public String index(Model model) {
        List<Product> products = productRepository.findAll();

        model.addAttribute("products", products);

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

        Product currentProduct = productRepository.getOne(product.getId());

        // Check product for errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("productName", currentProduct.getName());
            return "admin/categories/add";
        }

        // Check image file to make sure it is valid
        boolean fileOK = false;
        byte[] bytes = file.getBytes();
        String filename = file.getOriginalFilename();
        Path path = Paths.get("src/main/resources/static");

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
        }
        // Check if product exists, if so then display errors, otherwise set the new product slug, new product image, and save repo
        else if (productExists != null) {
            redirectAttributes.addFlashAttribute("message", "Product already exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
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
}
