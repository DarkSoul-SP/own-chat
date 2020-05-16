package ua.darksoul.testprojects.ownchat.domain.dto;

import lombok.ToString;
import lombok.Value;
import ua.darksoul.testprojects.ownchat.domain.Message;
import ua.darksoul.testprojects.ownchat.domain.User;
import ua.darksoul.testprojects.ownchat.domain.util.MessageHelper;

@Value
@ToString(of = {"id", "author", "likes", "meLiked"})
public class MessageDto {
    private Long id;
    private String text;
    private String tag;
    private User author;
    private String filename;
    private Long likes;
    private boolean meLiked;

    public MessageDto(Message message, Long likes, boolean meLiked) {
        this.id = message.getId();
        this.text = message.getText();
        this.tag = message.getTag();
        this.author = message.getAuthor();
        this.filename = message.getFilename();
        this.likes = likes;
        this.meLiked = meLiked;
    }

    public String getAuthorName(){
        return MessageHelper.getAuthorName(author);
    }
}
