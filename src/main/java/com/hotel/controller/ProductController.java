package com.hotel.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hotel.entity.Product;
import com.hotel.repository.ProductRepository;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // SHOW PRODUCTS (USER WISE)
    @GetMapping("/products")
    public String viewProducts(Model model, Principal principal) {

        String username = principal.getName();

        List<Product> products = productRepository.findByUsername(username);

        model.addAttribute("products", products);

        return "products";
    }

    // ADD PRODUCT PAGE
    @GetMapping("/add-product")
    public String addProductPage(Model model) {

        model.addAttribute("product", new Product());

        return "add-product";
    }

    // SAVE PRODUCT
    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute Product product, Principal principal) {

        String username = principal.getName();

        product.setUsername(username);

        productRepository.save(product);

        return "redirect:/products";
    }

    // DELETE PRODUCT
    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable Long id) {

        productRepository.deleteById(id);

        return "redirect:/products";
    }
}