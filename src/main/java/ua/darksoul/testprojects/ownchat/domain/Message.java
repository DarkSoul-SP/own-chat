package ua.darksoul.testprojects.ownchat.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ua.darksoul.testprojects.ownchat.domain.util.MessageHelper;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Please fill the message")
    @Length(max = 2048, message = "Message too long (more than 2kB)")
    @NonNull
    private String text;
    @NotBlank(message = "Please fill the tag")
    @Length(max = 255, message = "Tag too long (more than 255)")
    @NonNull
    private String tag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @NonNull
    private User author;

    private String filename;

//    @Lob
//    private byte[] file;

    @ManyToMany
    @JoinTable(
            name = "message_likes",
            joinColumns = { @JoinColumn( name = "message_id") },
            inverseJoinColumns = { @JoinColumn( name = "user_id")}
    )
    private Set<User> likes = new HashSet<>();

    public String getAuthorName(){
        return MessageHelper.getAuthorName(author);
    }
}
