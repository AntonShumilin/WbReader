package com.WbReader.Controller;

import com.WbReader.CustomExeptions.UserAlreadyExistsException;
import com.WbReader.Data.Role;
import com.WbReader.Data.User;
import com.WbReader.Data.UserRepo;
import com.WbReader.Services.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser
            (@RequestParam String username,
             @RequestParam String password,
             @RequestParam String confirmPassword,
             Model model
            ) throws UserAlreadyExistsException {
        if (password == null || !password.equals(confirmPassword)) {
            model.addAttribute("message", "Password is empty or not equals");
            return "register";
        } else if (password.length() < 3) {
            model.addAttribute("message", "Password must be at least 3 characters");
            return "register";
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode(password));
        user.setRoles(Stream.of(Role.USER).collect(Collectors.toSet()));
        userService.addNewUser(user);
        return "login";
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

}
