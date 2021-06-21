package com.WbReader.Controller;

import com.WbReader.Configuration.CustomGoogleOAuthProvider;
import com.WbReader.CustomExeptions.CustomException;
import com.WbReader.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;

@Controller
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    CustomGoogleOAuthProvider customGoogleOAuthProvider;

    @GetMapping("/")
    public String root() {
        return "redirect:/account";
    }

    @GetMapping("/register")
    public String register(Principal principal){
        if (principal != null) {
            return "redirect:/account";
        }
        return "register";
    }

    @PostMapping("/register")
    public String registerUser (@RequestParam String username, @RequestParam String password,
                                @RequestParam String confirmPassword, Model model) {
        ArrayList<String> errorMsgList = new ArrayList<>();
        if (userService.findByUserName(username).isPresent()) {
            errorMsgList.add(String.format("User \"%s\" already exists. Please, use another username", username));
        }
        if (password == null || password.isEmpty() || !password.equals(confirmPassword)) {
            errorMsgList.add("Password is empty or not equals");
        } else if (password.length() < 3) {
            errorMsgList.add("Password must be at least 3 characters");
        }
        if (!errorMsgList.isEmpty()) {
            model.addAttribute("messageList", errorMsgList);
            return "register";
        }
        userService.addNewUser(username, password, false);
        return "login";
    }

    @GetMapping("/oauth")
    public String oauth (@RequestParam(name = "code", required = false) String code) throws CustomException {
        if (code == null || code.isEmpty())  {
            return "redirect:" + customGoogleOAuthProvider.getOAuthRequest();
        } else {
            String accessToken = customGoogleOAuthProvider.getAccessToken(code);
            String email = customGoogleOAuthProvider.getUserUnfo(accessToken);
            if (email != null && !email.isEmpty()) {
                customGoogleOAuthProvider.authenticate(email);
            }
            return "redirect:/account";
        }
    }

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }
}
