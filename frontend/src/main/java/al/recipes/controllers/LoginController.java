package al.recipes.controllers;

import al.recipes.models.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;


@Controller
@RequestMapping("/")
public class LoginController {
    
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    
    @GetMapping(value = {"/login"})
    public String getLogin(Model model) {
        return "login";
    }
    
    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
    
    @GetMapping("/denied")
    public String denied(Model model) {
        model.addAttribute("denied", true);
        return "login";
    }
    
    @GetMapping("/signup")
    public String signup(Model model, @Valid @RequestBody String params) {
        
        String url = "http://localhost/api/signup?" + params;
        ResponseEntity<?> response = new RestTemplate().postForEntity(url, null, Users.class);
        model.addAttribute("signup_done", true);
        return "login";
    }
    
    @GetMapping("/logoutSuccessful")
    public String logout(Model model) {
        model.addAttribute("logout", true);
        return "login";
    }
}

