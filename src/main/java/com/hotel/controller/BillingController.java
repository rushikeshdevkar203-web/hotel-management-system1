package com.hotel.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hotel.entity.Orders;
import com.hotel.entity.Room;
import com.hotel.repository.OrderRepository;
import com.hotel.repository.RoomRepository;

@Controller
public class BillingController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RoomRepository roomRepository;

    // BILLING PAGE
    @GetMapping("/billing")
    public String billingPage() {
        return "billing";
    }

    // GENERATE BILL
    @GetMapping("/generateBill")
    public String generateBill(@RequestParam String roomNumber,
                               Model model,
                               Principal principal) {

        String username = principal.getName();

        // Get food orders for this room and user
        List<Orders> orders = orderRepository.findByRoomNumberAndUsername(roomNumber, username);

        double foodTotal = 0;

        for (Orders order : orders) {
            foodTotal += order.getTotalPrice();
        }

        // Get room details for that user
        Room room = roomRepository.findByRoomNumberAndUsername(roomNumber, username);

        double roomRent = 0;

        if (room != null) {
            roomRent = room.getPrice();
        }

        // Total bill
        double totalBill = foodTotal + roomRent;

        // Send to view
        model.addAttribute("orders", orders);
        model.addAttribute("roomNumber", roomNumber);
        model.addAttribute("foodTotal", foodTotal);
        model.addAttribute("roomRent", roomRent);
        model.addAttribute("totalBill", totalBill);

        return "bill";
    }
}