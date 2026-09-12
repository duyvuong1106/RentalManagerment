package com.nldv.rentalroom.controller;

import com.nldv.rentalroom.enums.UserRole;
import com.nldv.rentalroom.enums.UserStatus;
import com.nldv.rentalroom.pojo.User;
import com.nldv.rentalroom.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(
            UserService userService,
            PasswordEncoder passwordEncoder) {

        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registerForm(Model model) {

        model.addAttribute("user", new User());

        return "register";
    }

    @PostMapping("/register")
    public String register(
            User user,
            Model model) {

        
        if (userService.existsByUsername(user.getUsername())) {

            model.addAttribute(
                    "error",
                    "Username đã tồn tại."
            );

            return "register";
        }

        
        if (userService.existsByEmail(user.getEmail())) {

            model.addAttribute(
                    "error",
                    "Email đã được sử dụng."
            );

            return "register";
        }

        
        user.setPassword(
                passwordEncoder.encode(user.getPassword())
        );

        
        user.setRole(UserRole.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);

        userService.save(user);

        return "redirect:/login?registerSuccess";
    }
}