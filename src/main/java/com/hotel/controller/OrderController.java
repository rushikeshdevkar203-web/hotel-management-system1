package com.hotel.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hotel.entity.Orders;
import com.hotel.entity.Product;
import com.hotel.repository.OrderRepository;
import com.hotel.repository.ProductRepository;

@Controller
public class OrderController {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private OrderRepository orderRepo;

    // OPEN ORDER PAGE
    @GetMapping("/order")
    public String showOrderPage(Model model, Principal principal){

        String username = principal.getName();

        List<Product> products = productRepo.findByUsername(username);

        model.addAttribute("products", products);

        return "order";
    }

    // SHOW ALL ORDERS
    @GetMapping("/orders")
    public String viewOrders(Model model, Principal principal){

        String username = principal.getName();

        List<Orders> orders = orderRepo.findByUsername(username);

        model.addAttribute("orders", orders);

        return "orders";
    }

    // SAVE ORDER
    @PostMapping("/order")
    public String placeOrder(@RequestParam Long productId,
                             @RequestParam String customerName,
                             @RequestParam String customerMobile,
                             @RequestParam String roomNumber,
                             @RequestParam int quantity,
                             Principal principal){

        String username = principal.getName();

        Product product = productRepo.findById(productId).orElse(null);

        if(product != null){

            product.setStockQuantity(product.getStockQuantity() - quantity);
            productRepo.save(product);

            Orders order = new Orders();

            order.setCustomerName(customerName);
            order.setCustomerMobile(customerMobile);
            order.setRoomNumber(roomNumber);

            order.setProductName(product.getName());
            order.setPrice(product.getPrice());
            order.setQuantity(quantity);
            order.setTotalPrice(product.getPrice() * quantity);

            order.setOrderTime(LocalDateTime.now());

            // IMPORTANT
            order.setUsername(username);

            orderRepo.save(order);
        }

        return "redirect:/orders";
    }

    // SAVE ORDER (Manual Form)
    @PostMapping("/saveOrder")
    public String saveOrder(@ModelAttribute Orders order, Principal principal) {

        String username = principal.getName();

        order.setOrderTime(LocalDateTime.now());

        order.setTotalPrice(order.getPrice() * order.getQuantity());

        order.setUsername(username);

        orderRepo.save(order);

        return "redirect:/orders";
    }
}