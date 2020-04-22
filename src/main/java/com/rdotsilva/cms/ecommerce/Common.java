package com.rdotsilva.cms.ecommerce;

import com.rdotsilva.cms.ecommerce.models.Cart;
import com.rdotsilva.cms.ecommerce.models.CategoryRepository;
import com.rdotsilva.cms.ecommerce.models.PageRepository;
import com.rdotsilva.cms.ecommerce.models.data.Category;
import com.rdotsilva.cms.ecommerce.models.data.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

@ControllerAdvice
public class Common {

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @ModelAttribute
    public void sharedData(Model model, HttpSession session) {

        List<Page> pages = pageRepository.findAllByOrderBySortingAsc();

        List<Category> categories = categoryRepository.findAll();

        boolean cartActive = false;

        // Check for active shopping cart data
        if (session.getAttribute("cart") != null) {
            HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>)session.getAttribute("cart");

            int itemsInCart = 0;
            double total = 0;

            for (Cart value : cart.values()) {
                itemsInCart += value.getQuantity();
                total += value.getQuantity() * Double.parseDouble(value.getPrice());
            }

            model.addAttribute("csize", itemsInCart);
            model.addAttribute("ctotal", total);

            cartActive = true;
         }

        model.addAttribute("cpages", pages);
        model.addAttribute("ccategories", categories);
        model.addAttribute("cartActive", cartActive);
    }
}
