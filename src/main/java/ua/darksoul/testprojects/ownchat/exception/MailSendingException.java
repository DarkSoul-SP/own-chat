package ua.darksoul.testprojects.ownchat.exception;/*
 *
 *@author DarkSoul
 */

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class MailSendingException extends RuntimeException {

    public MailSendingException(String message) {
        super(message);
    }
}
