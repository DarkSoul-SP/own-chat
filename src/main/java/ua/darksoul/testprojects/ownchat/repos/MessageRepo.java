package ua.darksoul.testprojects.ownchat.repos;

import org.springframework.data.repository.CrudRepository;
import ua.darksoul.testprojects.ownchat.domain.Message;

import java.util.List;

public interface MessageRepo extends CrudRepository<Message, Long> {
    List<Message> findByTag(String tag);
}
