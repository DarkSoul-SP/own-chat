package ua.darksoul.testprojects.ownchat.controller;

import io.sentry.Sentry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import ua.darksoul.testprojects.ownchat.domain.User;
import ua.darksoul.testprojects.ownchat.service.UserService;
import ua.darksoul.testprojects.ownchat.util.ExceptionUtil;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Map;

@Controller
public class RegistrationController {
    private static final String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${recaptcha.secret}")
    private String recaptchaSecret;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam(value = "g-recaptcha-response", defaultValue = "notPresent") String captchaResponse,
            @Valid User user,
            BindingResult bindingResult,
            Model model
    ){
        //TODO fix bug with recaptcha
        /*String url = String.format(CAPTCHA_URL, recaptchaSecret, captchaResponse);

        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);

        if(!response.isSuccess()){
            model.addAttribute("captchaError", "Fill captcha");
        }*/

        if(user.getPassword() != null && !user.getPassword().equals(user.getConfirmPassword())){
            model.addAttribute("passwordError", "Password are different.");
            return "registration";
        }

//        if(bindingResult.hasErrors() || !response.isSuccess()){
        if(bindingResult.hasErrors()){
            Map<String, String> errors = ExceptionUtil.getErrors(bindingResult);

            model.mergeAttributes(errors);

            return "registration";
        }

        if(!userService.addUser(user)){
            model.addAttribute("usernameError", "User with this username already exists.");
            return "registration";
        }

        String explanatoryMessage = String.format("Welcome, %s. Letter with activation code was sent on your email. \n" +
                "Check this to complete the registration.", user.getUsername());
        model.addAttribute("messageType", "success");
        model.addAttribute("message", explanatoryMessage);

        Sentry.capture("New user was created: " + user);
        return "login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){
        boolean isActivated = userService.activateUser(code);

        if(isActivated){
            model.addAttribute("messageType", "success");
            model.addAttribute("message", "User successfully activated.");
        }else{
            model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation code is not found. \n Maybe your account have already activated.");
        }

        return "login";
    }
}
