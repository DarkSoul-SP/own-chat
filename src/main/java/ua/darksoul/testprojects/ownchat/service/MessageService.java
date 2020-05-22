package ua.darksoul.testprojects.ownchat.service;

import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import ua.darksoul.testprojects.ownchat.domain.Message;
import ua.darksoul.testprojects.ownchat.domain.User;
import ua.darksoul.testprojects.ownchat.domain.dto.MessageDto;
import ua.darksoul.testprojects.ownchat.repo.MessageRepo;
import ua.darksoul.testprojects.ownchat.util.ExceptionUtil;

import java.io.IOException;

@Service
public class MessageService {
    @Autowired
    private MessageRepo messageRepo;
    @Autowired
    private FileService fileService;


    public Page<MessageDto> messageList(Pageable pageable, String filter, User user) {
        if(filter !=null && !filter.isEmpty()) {
            return messageRepo.findByTag(filter, pageable, user);
        } else{
            return messageRepo.findAll(pageable, user);
        }
    }

    public Page<MessageDto> messageListForUser(Pageable pageable, User currentUser, User author) {
        return messageRepo.findByUser(pageable, author, currentUser);
    }

    public void saveMessage(Message message) {
        messageRepo.save(message);
    }

    public void deleteMessage(Message message) {
        messageRepo.delete(message);
    }

    public void createMessage(User user, Message message, BindingResult bindingResult, Model model, MultipartFile file) throws IOException {
        message.setAuthor(user);

        if(bindingResult.hasErrors()){
            val mapErrors = ExceptionUtil.getErrors(bindingResult);

            model.mergeAttributes(mapErrors);
            model.addAttribute("message", message);
        } else {
            fileService.saveImg(message, file);
            saveMessage(message);

            model.addAttribute("message", null);
        }
    }
}
