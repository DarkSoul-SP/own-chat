package ua.darksoul.testprojects.ownchat.controller;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import ua.darksoul.testprojects.ownchat.domain.Message;
import ua.darksoul.testprojects.ownchat.domain.User;
import ua.darksoul.testprojects.ownchat.service.FileService;
import ua.darksoul.testprojects.ownchat.service.MessageService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Controller
public class MessageController {

    @Autowired
    private FileService fileService;

    @Autowired
    private MessageService messageService;

    @GetMapping("/")
    public String greeting() {
        return "greeting";
    }

    @GetMapping("/main")
    public String main(
            @RequestParam(required = false, defaultValue = "") String filter,
            Model model,
            @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal User user
    ){
        val page = messageService.messageList(pageable, filter, user);

        model.addAttribute("page", page);
        model.addAttribute("url", "/main");
        model.addAttribute("itemsCount", page.getSize());
        model.addAttribute("filter", filter);

        return "main";
    }

    @PostMapping("/main")
    public String addNewMessage(
            @AuthenticationPrincipal User user,
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam(required = false, defaultValue = "") String filter,
            @RequestParam("file") MultipartFile file,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ) throws IOException {
        messageService.createMessage(user, message, bindingResult, model, file);

        val page = messageService.messageList(pageable, filter, user);

        model.addAttribute("page", page);
        model.addAttribute("itemsCount", page.getSize());

        return "main";
    }

    @GetMapping("/user-messages/{author}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User author,
            Model model,
            @RequestParam(required = false) Message message,
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable
    ){
        val page = messageService.messageListForUser(pageable, currentUser, author);

        model.addAttribute("isCurrentUser", currentUser.equals(author));
        model.addAttribute("userChannel", author);
        model.addAttribute("subscriptionsCount", author.getSubscriptions().size());
        model.addAttribute("subscribersCount", author.getSubscribers().size());
        model.addAttribute("isSubscriber", author.getSubscribers().contains(currentUser));
        model.addAttribute("url", "/user-messages/" + author.getId());
        model.addAttribute("message", message);
        model.addAttribute("page", page);
        model.addAttribute("itemsCount", page.getSize());

        return "userMessages";
    }

    @PostMapping("/user-messages/{user}")
    public String updateMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long user,
            @RequestParam("id") Message messageFromDb,
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        if(messageFromDb != null && messageFromDb.getAuthor().equals(currentUser)){
            if(!StringUtils.isEmpty(message.getText())){
                messageFromDb.setText(message.getText());
            }

            if(!StringUtils.isEmpty(message.getTag())){
                messageFromDb.setTag(message.getTag());
            }

            fileService.deleteFile(messageFromDb.getFilename());
            fileService.saveImg(messageFromDb, file);

            messageService.saveMessage(messageFromDb);
        } else {
            messageService.createMessage(currentUser, message, bindingResult, model, file);
        }

        return "redirect:/user-messages/" + user;
    }

    @GetMapping("/delete-message/{author}/{message}")
    public String deleteMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable User author,
            @PathVariable Message message,
            RedirectAttributes redirectAttributes,
            @RequestHeader(required = false) String referer
    ) {
        if((author.equals(currentUser) || currentUser.isAdmin()) && message != null){
            fileService.deleteFile(message.getFilename());
            messageService.deleteMessage(message);
        }

        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        components.getQueryParams()
                .entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));

        return "redirect:/main";
    }

    @GetMapping("/messages/{message}/like")
    public String like(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Message message,
            RedirectAttributes redirectAttributes,
            @RequestHeader(required = false) String referer
    ) {
        Set<User> likes = message.getLikes();

        if(likes.contains(currentUser)) {
            likes.remove(currentUser);
        } else {
            likes.add(currentUser);
        }

        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        components.getQueryParams()
                .entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));

        return "redirect:" + components.getPath();
    }
}
