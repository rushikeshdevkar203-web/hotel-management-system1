package com.hotel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hotel.entity.Room;
import com.hotel.entity.Booking;
import com.hotel.repository.RoomRepository;
import com.hotel.repository.BookingRepository;

@Controller
public class RoomController {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    // Show All Rooms
    @GetMapping("/rooms")
    public String viewRooms(Model model) {
        model.addAttribute("rooms", roomRepository.findAll());
        return "rooms";
    }

    // Show Add Room Form
    @GetMapping("/add-room")
    public String showAddRoomForm(Model model) {
        model.addAttribute("room", new Room());
        return "add-room";
    }

    // Save Room
    @PostMapping("/save-room")
    public String saveRoom(@ModelAttribute Room room) {
        roomRepository.save(room);
        return "redirect:/rooms";
    }

    // Show Update Form
    @GetMapping("/edit-room/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Room Id: " + id));

        model.addAttribute("room", room);

        return "edit-room";
    }

    // Update Room and Update Booking Room Number
    @PostMapping("/update-room/{id}")
    public String updateRoom(@PathVariable Long id, @ModelAttribute Room room) {

        Room existingRoom = roomRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Room Id: " + id));

        String oldRoomNumber = existingRoom.getRoomNumber();
        String newRoomNumber = room.getRoomNumber();

        // Update room details
        existingRoom.setRoomNumber(newRoomNumber);
        existingRoom.setRoomType(room.getRoomType());
        existingRoom.setPrice(room.getPrice());
        existingRoom.setStatus(room.getStatus());

        roomRepository.save(existingRoom);

        // If room number changed → update bookings
        if (!oldRoomNumber.equals(newRoomNumber)) {

            List<Booking> bookings = bookingRepository.findByRoomNumber(oldRoomNumber);

            for (Booking booking : bookings) {
                booking.setRoomNumber(newRoomNumber);
            }

            bookingRepository.saveAll(bookings);
        }

        return "redirect:/rooms";
    }
}