package ua.darksoul.testprojects.ownchat.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.darksoul.testprojects.ownchat.domain.User;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);

    User findByActivationCode(String code);
}
