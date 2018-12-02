package al.recipes.security;

import al.recipes.rest.controllers.TagControllerRest;
import al.recipes.soap.SoapClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/")
public class LoginController {
    
    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private SoapClient categorySoapClient;
    @Autowired
    private TagControllerRest tagControllerRest;
    
    @RequestMapping(value = {"/login"})
    public String getLogin(Model model) {
        return "login";
    }
    
    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }
    
}

