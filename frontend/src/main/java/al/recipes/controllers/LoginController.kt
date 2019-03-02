package al.recipes.controllers

import al.recipes.models.Users
import io.swagger.annotations.ApiOperation
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.client.RestTemplate
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid


@Controller
@RequestMapping("/")
class LoginController {

    @GetMapping(value = ["/login"])
    @ApiOperation(value = "Get login template")
    fun getLogin(model: Model): String {
        return "login"
    }

    @GetMapping("/login-error")
    @ApiOperation(value = "Set error message to be diplayed in the login template")
    fun loginError(model: Model): String {
        model.addAttribute("loginError", true)
        return "login"
    }

    @GetMapping("/denied")
    @ApiOperation(value = "Set denied message to be diplayed in the login template")
    fun denied(model: Model): String {
        model.addAttribute("denied", true)
        return "login"
    }

    @PostMapping("/signup")
    @ApiOperation(value = "Get the signup template")
    fun signup(model: Model, @Valid @RequestBody params: String, request: HttpServletRequest): String {
        val https = request.scheme as String
        val urlConn = https + "://" + request.serverName

        val url = "$urlConn/api/signup?$params"
        RestTemplate().postForEntity(url, null, Users::class.java)
        model.addAttribute("signup_done", true)
        return "login"
    }

    @GetMapping("/logoutSuccessful")
    @ApiOperation(value = "Logout and redirect to the login template")
    fun logout(model: Model): String {
        model.addAttribute("logout", true)
        return "login"
    }

    companion object {
        private val log = LoggerFactory.getLogger(LoginController::class.java)
    }
}

