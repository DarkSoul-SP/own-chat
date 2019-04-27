package ua.darksoul.testprojects.ownchat.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Message {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Please fill the message")
    @Length(max = 2048, message = "Message too long (more than 2kB)")
    @NonNull
    private String text;
    @Length(max = 255, message = "Tag too long (more than 255)")
    @NonNull
    private String tag;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    @NonNull
    private User author;

    private String filename;

    public String getAuthorName(){
        return author != null ? author.getUsername() : "<none>";
    }
}
