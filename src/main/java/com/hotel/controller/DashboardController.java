package com.hotel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.hotel.repository.RoomRepository;
import com.hotel.repository.BookingRepository;
import com.hotel.repository.OrderRepository;
import com.hotel.repository.ProductRepository;

@Controller
public class DashboardController {

    @Autowired
    private RoomRepository roomRepo;

    @Autowired
    private BookingRepository bookingRepo;

    @Autowired
    private ProductRepository productRepo;
    
    @Autowired
    private OrderRepository orderRepo;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        // Room Data
    	long totalProducts = productRepo.count();
        long totalOrders = orderRepo.count();   // 🔥 NEW LINE
    	long totalRooms = roomRepo.count();
        long totalBookings = bookingRepo.count();

        double totalMenuValue = productRepo.findAll()
                .stream()
                .mapToDouble(p -> p.getPrice() * p.getStockQuantity())
                .sum();

        // Add attributes to model
        model.addAttribute("totalRooms", totalRooms);
        model.addAttribute("totalBookings", totalBookings);
        model.addAttribute("totalProducts", totalProducts);
        model.addAttribute("totalMenuValue", totalMenuValue);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("menuList", productRepo.findAll());

        return "dashboard";
    }
}