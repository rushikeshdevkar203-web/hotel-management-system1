package com.hotel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.hotel.entity.User;
import com.hotel.repository.UserRepository;
import com.hotel.service.UserService;

@SpringBootApplication
public class HotelManagementSystem1Application {

	public static void main(String[] args) {
		SpringApplication.run(HotelManagementSystem1Application.class, args);
	}
	
	@Bean
	CommandLineRunner run(UserRepository repo, BCryptPasswordEncoder encoder) {
	    return args -> {

	        if (repo.findByUsername("Rushi") == null) {
	            User user = new User();
	            user.setUsername("Rushi");
	            user.setPassword(encoder.encode("4243"));
	            user.setRole("ADMIN");
	            repo.save(user);
	        }
	    };
	}

}
