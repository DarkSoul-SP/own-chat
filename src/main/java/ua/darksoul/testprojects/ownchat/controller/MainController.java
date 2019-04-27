package ua.darksoul.testprojects.ownchat.controller;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ua.darksoul.testprojects.ownchat.domain.Message;
import ua.darksoul.testprojects.ownchat.domain.User;
import ua.darksoul.testprojects.ownchat.domain.dto.CaptchaResponseDto;
import ua.darksoul.testprojects.ownchat.repos.MessageRepo;
import ua.darksoul.testprojects.ownchat.service.FileService;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Controller
public class MainController {
    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private FileService fileService;

    @GetMapping("/")
    public String greeting(Map<String, Object> model) {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model){
        Iterable<Message> messages = messageRepo.findAll();

        if(filter !=null && !filter.isEmpty()) {
            messages = messageRepo.findByTag(filter);
        }else{
            messages = messageRepo.findAll();
        }

        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);

        return "main";
    }

    @PostMapping("/main")
    public String addNewMessage(
            @AuthenticationPrincipal User user,
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        message.setAuthor(user);

        if(bindingResult.hasErrors()){
            Map<String, String> mapErrors = UtillsController.getErrors(bindingResult);

            model.mergeAttributes(mapErrors);
            model.addAttribute("message", message);
        } else {
            fileService.saveFile(message, file);

            model.addAttribute("message", null);

            messageRepo.save(message);
        }

        val messages = messageRepo.findAll();

        model.addAttribute("messages", messages);

        return "main";
    }

    @GetMapping("/user-messages/{user}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User user,
            Model model,
            @RequestParam(required = false) Message message
    ){
        Set<Message> messages = user.getMessages();

        model.addAttribute("message", message);
        model.addAttribute("messages", messages);
        model.addAttribute("isCurrentUser", currentUser.equals(user));
        model.addAttribute("userChannel", user);
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));

        return "userMessages";
    }

    @PostMapping("/user-messages/{user}")
    public String updateMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long user,
            @RequestParam("id") Message message,
            @RequestParam("text") String text,
            @RequestParam("tag") String tag,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if(message.getAuthor().equals(currentUser)){
            if(!StringUtils.isEmpty(text)){
                message.setText(text);
            }

            if(!StringUtils.isEmpty(tag)){
                message.setTag(tag);
            }

            fileService.saveFile(message,file);

            messageRepo.save(message);
        }

        return "redirect:/user-messages/" + user;
    }
}
