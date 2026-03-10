package com.hotel.controller;

import com.hotel.entity.Orders;
import com.hotel.entity.Room;
import com.hotel.repository.OrderRepository;
import com.hotel.repository.RoomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BillingController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RoomRepository roomRepository;
    
    @GetMapping("/billing")
    public String billingPage() {
        return "billing";
    }

    // GENERATE BILL
    @GetMapping("/generateBill")
    public String generateBill(@RequestParam String roomNumber, Model model) {

        // Get food orders for this room
        List<Orders> orders = orderRepository.findByRoomNumber(roomNumber);

        double foodTotal = 0;

        // Calculate food total
        for (Orders order : orders) {
            foodTotal += order.getTotalPrice() * order.getQuantity();
        }

        // Get room details
        Room room = roomRepository.findByRoomNumber(roomNumber);

        double roomRent = 0;

        if (room != null) {
            roomRent = room.getPrice();
        }

        // Calculate total bill
        double totalBill = foodTotal + roomRent;

        // Send data to view
        model.addAttribute("orders", orders);
        model.addAttribute("roomNumber", roomNumber);
        model.addAttribute("foodTotal", foodTotal);
        model.addAttribute("roomRent", roomRent);
        model.addAttribute("totalBill", totalBill);

        return "bill";
    }
}