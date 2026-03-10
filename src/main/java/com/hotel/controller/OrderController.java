package com.hotel.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String showOrderPage(Model model){

        model.addAttribute("products", productRepo.findAll());

        return "order";
    }

    // SHOW ALL ORDERS
    @GetMapping("/orders")
    public String viewOrders(Model model){

        model.addAttribute("orders", orderRepo.findAll());

        return "orders";
    }

    // SAVE ORDER
    @PostMapping("/order")
    public String placeOrder(@RequestParam Long productId,
                             @RequestParam String customerName,
                             @RequestParam String customerMobile,
                             @RequestParam String roomNumber,
                             @RequestParam int quantity){

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

            orderRepo.save(order);
        }

        return "redirect:/orders";
    }
    
    @PostMapping("/saveOrder")
    public String saveOrder(@ModelAttribute Orders order) {

        order.setOrderTime(LocalDateTime.now());

        order.setTotalPrice(order.getPrice() * order.getQuantity());

        orderRepo.save(order);

        return "redirect:/orders";
    }
}