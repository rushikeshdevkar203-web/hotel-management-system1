package com.hotel.controller;

import com.hotel.entity.Booking;
import com.hotel.entity.Room;
import com.hotel.repository.BookingRepository;
import com.hotel.repository.RoomRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BookingController {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    // OPEN BOOKING PAGE
    @GetMapping("/booking")
    public String bookingPage(Model model) {

        // show only available rooms
        List<Room> rooms = roomRepository.findByStatus("Available");

        model.addAttribute("rooms", rooms);

        return "booking";
    }

    // SAVE BOOKING
    @PostMapping("/booking")
    public String bookRoom(
            @RequestParam String roomNumber,
            @RequestParam String customerName,
            @RequestParam String checkInDate,
            @RequestParam String checkOutDate,
            @RequestParam Double pricePerDay
    ) {

        Booking booking = new Booking();

        booking.setRoomNumber(roomNumber);
        booking.setCustomerName(customerName);
        booking.setCheckInDate(checkInDate);
        booking.setCheckOutDate(checkOutDate);
        booking.setPricePerDay(pricePerDay);
        booking.setStatusMessage("Booked");

        bookingRepository.save(booking);

        // update room status
        Room room = roomRepository.findByRoomNumber(roomNumber);
        if (room != null) {
            room.setStatus("Booked");
            roomRepository.save(room);
        }

        return "redirect:/bookings";
    }

    // SHOW BOOKINGS
    @GetMapping("/bookings")
    public String viewBookings(Model model) {

        List<Booking> list = bookingRepository.findAll();

        model.addAttribute("bookings", list);

        return "bookings";
    }

    // DELETE BOOKING
    @GetMapping("/deleteBooking/{id}")
    public String deleteBooking(@PathVariable Long id) {

        Booking booking = bookingRepository.findById(id).orElse(null);

        if (booking != null) {

            String roomNumber = booking.getRoomNumber();

            bookingRepository.deleteById(id);

            Room room = roomRepository.findByRoomNumber(roomNumber);
            if (room != null) {
                room.setStatus("Available");
                roomRepository.save(room);
            }
        }

        return "redirect:/bookings";
    }

    // EDIT BOOKING PAGE
    @GetMapping("/editBooking/{id}")
    public String editBooking(@PathVariable Long id, Model model) {

        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid booking ID"));

        List<Room> rooms = roomRepository.findAll();

        model.addAttribute("booking", booking);
        model.addAttribute("rooms", rooms);

        return "edit-booking";
    }
    // UPDATE BOOKING
    @PostMapping("/updateBooking/{id}")
    public String updateBooking(@PathVariable Long id, @ModelAttribute Booking booking) {

        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid booking ID"));

        String oldRoomNumber = existingBooking.getRoomNumber();
        String newRoomNumber = booking.getRoomNumber();

        // old room available
        Room oldRoom = roomRepository.findByRoomNumber(oldRoomNumber);
        if (oldRoom != null) {
            oldRoom.setStatus("Available");
            roomRepository.save(oldRoom);
        }

        // new room booked
        Room newRoom = roomRepository.findByRoomNumber(newRoomNumber);
        if (newRoom != null) {
            newRoom.setStatus("Booked");
            roomRepository.save(newRoom);
        }

        existingBooking.setRoomNumber(newRoomNumber);
        existingBooking.setCustomerName(booking.getCustomerName());
        existingBooking.setCheckInDate(booking.getCheckInDate());
        existingBooking.setCheckOutDate(booking.getCheckOutDate());
        existingBooking.setPricePerDay(booking.getPricePerDay());

        bookingRepository.save(existingBooking);

        return "redirect:/bookings";
    }
} 