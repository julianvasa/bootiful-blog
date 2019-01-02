package al.recipes.controllers;

import al.recipes.models.Users;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;


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
    
    @PostMapping("/signup")
    public String signup(Model model, @Valid @RequestBody String params) {
        String fullname = params.split("&")[0].split("=")[1];
        String user = params.split("&")[1].split("=")[1];
        String password = params.split("&")[2].split("=")[1];
        
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
        Map map = new HashMap<String, String>();
        map.put("Content-Type", "application/json");
        
        headers.setAll(map);
        
        Map req_payload = new HashMap();
        req_payload.put("user", user);
        req_payload.put("fullname", fullname);
        req_payload.put("password", password);
        
        HttpEntity<?> request = new HttpEntity<>(req_payload, headers);
        String url = "http://localhost/api/signup?" + params;
        
        ResponseEntity<?> response = new RestTemplate().postForEntity(url, null, Users.class);
        model.addAttribute("signup_done", true);
        return "login";
    }
    
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

