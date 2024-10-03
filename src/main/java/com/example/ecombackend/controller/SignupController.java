package com.example.ecombackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.ecombackend.service.ApiResponse;
import com.example.ecombackend.service.SearchObj;
import com.example.ecombackend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.ecombackend.model.User;
import com.example.ecombackend.service.*;
@RestController
@RequestMapping("/api")
public class SignupController {

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ApiResponse signup(@RequestBody User user) {
        boolean isSaved = userService.registerUser(user);
        
        if (isSaved) {
            return new ApiResponse(true, "User registered successfully!");
        } else {
            return new ApiResponse(false, "Error registering user!");
        }
    }
    
    @GetMapping("/checkDuplicate/{name}")
    public ApiResponse isDuplicate(@PathVariable String name) {
        return new ApiResponse(userService.isUsernameTaken(name), userService.isUsernameTaken(name)?"Duplicate":"Not Duplicate");

    	
    }
    @PostMapping("/isauth")
    public ApiResponse accessDashboard(@RequestBody User user) {
        if (userService.isAuthenticated(user.getName())) {
            return new ApiResponse(true,"Access granted to dashboard for user");
        }
        return new ApiResponse(false,"Invalid session. Please log in.");

       
    }
    @PostMapping("/auth")
    public ApiResponse login(@RequestBody User user) {
    	return userService.login(user.getEmail(), user.getPassword());
    }
    @PostMapping("/logout")
    public ApiResponse logout(@RequestBody User user) {
    	if(userService.logout(user.getName()))
    	return new ApiResponse(true,"Logged Out");
    	else
        	return new ApiResponse(false,"Logout Failed");

    }
}

