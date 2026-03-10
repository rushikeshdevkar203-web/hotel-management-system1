package com.hotel.controller;

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
    private ProductRepository productRepo;

    @GetMapping("/products")
    public String showProducts(Model model) {

        List<Product> products = productRepo.findAll();
        model.addAttribute("products", products);

        return "products";
    }
    @GetMapping("/add-product")
    public String addProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "add-product";
    }

    @PostMapping("/save-product")
    public String saveProduct(@ModelAttribute Product product) {
        productRepo.save(product);
        return "redirect:/products";
    }
    
    @GetMapping("/deleteProduct/{id}")
    public String deleteProduct(@PathVariable Long id) {

        productRepo.deleteById(id);

        return "redirect:/products";
    }
}