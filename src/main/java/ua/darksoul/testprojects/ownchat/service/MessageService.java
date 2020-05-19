package ua.darksoul.testprojects.ownchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.darksoul.testprojects.ownchat.domain.Message;
import ua.darksoul.testprojects.ownchat.domain.User;
import ua.darksoul.testprojects.ownchat.domain.dto.MessageDto;
import ua.darksoul.testprojects.ownchat.repo.MessageRepo;

@Service
public class MessageService {
    @Autowired
    private MessageRepo messageRepo;

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
}
