package al.recipes.rest.controllers;

import al.recipes.models.Users;
import al.recipes.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserControllerRest {
    
    @Autowired
    UserService userService;
    
    @PostMapping("/signup")
    public void signup(@RequestParam(value = "user") String user, @RequestParam(value = "password") String password, @RequestParam(value = "fullname") String fullname) {
        userService.signup(user, password, fullname);
    }
    
    @GetMapping("/users/{username}")
    public Users loadUserByUsername(@PathVariable(value = "username") String username) {
        return userService.loadUserByUsername(username);
    }
}
