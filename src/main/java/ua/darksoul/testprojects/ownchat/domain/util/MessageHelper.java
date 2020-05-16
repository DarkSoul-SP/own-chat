package ua.darksoul.testprojects.ownchat.domain.util;

import ua.darksoul.testprojects.ownchat.domain.User;

public abstract class MessageHelper {
    public static String getAuthorName(User author){
        return author != null ? author.getUsername() : "<none>";
    }
}
