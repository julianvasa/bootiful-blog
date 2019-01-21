package al.recipes.controllers;

import al.recipes.models.Users;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@Controller
@RequestMapping("/")
public class LoginController {
    
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    
    @GetMapping(value = {"/login"})
    @ApiOperation(value = "Get login template")
    public String getLogin(Model model) {
        return "login";
    }
    
    @GetMapping("/login-error")
    @ApiOperation(value = "Set error message to be diplayed in the login template")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
    
    @GetMapping("/denied")
    @ApiOperation(value = "Set denied message to be diplayed in the login template")
    public String denied(Model model) {
        model.addAttribute("denied", true);
        return "login";
    }
    
    @PostMapping("/signup")
    @ApiOperation(value = "Get the signup template")
    public String signup(Model model, @Valid @RequestBody String params, HttpServletRequest request) {
        String https = (String) request.getScheme();
        String urlConn = https + "://" + request.getServerName();
        
        String url = urlConn + "/api/signup?" + params;
        ResponseEntity<?> response = new RestTemplate().postForEntity(url, null, Users.class);
        model.addAttribute("signup_done", true);
        return "login";
    }
    
    @GetMapping("/logoutSuccessful")
    @ApiOperation(value = "Logout and redirect to the login template")
    public String logout(Model model) {
        model.addAttribute("logout", true);
        return "login";
    }
}

