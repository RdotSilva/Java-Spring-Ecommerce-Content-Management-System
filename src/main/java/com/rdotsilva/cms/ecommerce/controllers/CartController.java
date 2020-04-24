package com.rdotsilva.cms.ecommerce.controllers;

import com.rdotsilva.cms.ecommerce.models.Cart;
import com.rdotsilva.cms.ecommerce.models.ProductRepository;
import com.rdotsilva.cms.ecommerce.models.data.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@Controller
@RequestMapping("/cart")
@SuppressWarnings("unchecked")
public class CartController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/add/{id}")
    public String add(@PathVariable int id, HttpSession session, Model model) {

        Product product = productRepository.getOne(id);

        // Check for current session is set
        if (session.getAttribute("cart") == null) {

            // Create new cart and add first item
            HashMap<Integer, Cart> cart = new HashMap<>();
            cart.put(id, new Cart(id, product.getName(), product.getPrice(), 1, product.getImage()));
            session.setAttribute("cart", cart);

        } else {
            // Add items to current existing cart
            HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>)session.getAttribute("cart");
            if (cart.containsKey(id)) {
                int quantity = cart.get(id).getQuantity();
                cart.put(id, new Cart(id, product.getName(), product.getPrice(), ++quantity, product.getImage()));
            } else {
                cart.put(id, new Cart(id, product.getName(), product.getPrice(), 1, product.getImage()));
                session.setAttribute("cart", cart);
            }
        }

        // Get contents of cart
        HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>)session.getAttribute("cart");
        int itemsInCart = 0;
        double total = 0;

        for (Cart value : cart.values()) {
            itemsInCart += value.getQuantity();
            total += value.getQuantity() * Double.parseDouble(value.getPrice());
        }

        model.addAttribute("size", itemsInCart);
        model.addAttribute("total", total);

        return "cart_view";
    }

    @RequestMapping("/view")
    public String view(HttpSession session, Model model) {

        if (session.getAttribute("cart") == null) {
            return "redirect:/";
        }

        // Get contents of cart
        HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>)session.getAttribute("cart");
        model.addAttribute("cart", cart);

        return null;
    }
}
