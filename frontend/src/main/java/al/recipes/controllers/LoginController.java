package al.recipes.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
public class LoginController {
    
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    
    @RequestMapping(value = {"/login"})
    public String getLogin(Model model) {
        return "login";
    }
    
    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
    
    @RequestMapping("/denied")
    public String denied(Model model) {
        model.addAttribute("denied", true);
        return "login";
    }
    
    /*@PostMapping("/signup")
    public Recipes signup(@Valid @RequestBody Users user) {
        return userSe.save(recipe);
    }
    */
    /*@RequestMapping("/profile")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public String profile(Model model) {
        return "home";
    }
    */
    @RequestMapping("/logoutSuccessful")
    public String logout(Model model) {
        model.addAttribute("logout", true);
        return "login";
    }
    
}

