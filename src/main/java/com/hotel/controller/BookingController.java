package com.hotel.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.hotel.entity.Booking;
import com.hotel.entity.Room;
import com.hotel.repository.BookingRepository;
import com.hotel.repository.RoomRepository;

@Controller
public class BookingController {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BookingRepository bookingRepository;

    // OPEN BOOKING PAGE
    @GetMapping("/booking")
    public String bookingPage(Model model, Principal principal) {

        String username = principal.getName();

        List<Room> rooms = roomRepository.findByUsernameAndStatus(username, "Available");

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
            @RequestParam Double pricePerDay,
            Principal principal
    ) {

        String username = principal.getName();

        Booking booking = new Booking();

        booking.setRoomNumber(roomNumber);
        booking.setCustomerName(customerName);
        booking.setCheckInDate(checkInDate);
        booking.setCheckOutDate(checkOutDate);
        booking.setPricePerDay(pricePerDay);
        booking.setStatusMessage("Booked");
        booking.setUsername(username);

        bookingRepository.save(booking);

        Room room = roomRepository.findByRoomNumberAndUsername(roomNumber, username);

        if (room != null) {
            room.setStatus("Booked");
            roomRepository.save(room);
        }

        return "redirect:/bookings";
    }

    // SHOW BOOKINGS
    @GetMapping("/bookings")
    public String viewBookings(Model model, Principal principal) {

        String username = principal.getName();

        List<Booking> bookings = bookingRepository.findByUsername(username);

        model.addAttribute("bookings", bookings);

        return "bookings";
    }

    // DELETE BOOKING
    @GetMapping("/deleteBooking/{id}")
    public String deleteBooking(@PathVariable Long id) {

        Booking booking = bookingRepository.findById(id).orElse(null);

        if (booking != null) {

            String roomNumber = booking.getRoomNumber();
            String username = booking.getUsername();

            bookingRepository.deleteById(id);

            Room room = roomRepository.findByRoomNumberAndUsername(roomNumber, username);

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

        Booking booking = bookingRepository.findById(id).orElse(null);

        if (booking == null) {
            return "redirect:/bookings";
        }

        List<Room> rooms = roomRepository.findByUsername(booking.getUsername());

        model.addAttribute("booking", booking);
        model.addAttribute("rooms", rooms);

        return "edit-booking";
    }

    // UPDATE BOOKING
    @PostMapping("/updateBooking/{id}")
    public String updateBooking(@PathVariable Long id, @ModelAttribute Booking booking) {

        Booking existingBooking = bookingRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid booking ID"));

        String username = existingBooking.getUsername();

        String oldRoomNumber = existingBooking.getRoomNumber();
        String newRoomNumber = booking.getRoomNumber();

        // old room available
        Room oldRoom = roomRepository.findByRoomNumberAndUsername(oldRoomNumber, username);

        if (oldRoom != null) {
            oldRoom.setStatus("Available");
            roomRepository.save(oldRoom);
        }

        // new room booked
        Room newRoom = roomRepository.findByRoomNumberAndUsername(newRoomNumber, username);

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