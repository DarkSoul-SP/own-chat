package ua.darksoul.testprojects.ownchat.service;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import ua.darksoul.testprojects.ownchat.domain.Role;
import ua.darksoul.testprojects.ownchat.domain.User;
import ua.darksoul.testprojects.ownchat.repo.UserRepo;

import java.util.Collections;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepo userRepo;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private MailSender mailSender;

    @Test
    public void addUser() {
        User user = new User();
        user.setEmail("exam@mail.com");

        boolean isUserWasAdded = userService.addUser(user);

        Assert.assertTrue(isUserWasAdded);
        Assert.assertNotNull(user.getActivationCode());
        Assert.assertTrue(CoreMatchers.is(user.getRoles()).matches(Collections.singleton(Role.USER)));

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
        Mockito.verify(mailSender,Mockito.times(1))
                .send(
                        ArgumentMatchers.eq(user.getEmail()),
                        ArgumentMatchers.eq("Activation code"),
                        ArgumentMatchers.contains("Welcome to OwnChat.")
                );
    }

    @Test
    public void addUserFailed() {
        User user = new User();
        user.setUsername("Colin");
        Mockito.doReturn(new User())
                .when(userRepo)
                .findByUsername("Colin");

        boolean isUserWasAdded = userService.addUser(user);

        Assert.assertFalse(isUserWasAdded);
        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(mailSender,Mockito.times(0))
                .send(
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString(),
                        ArgumentMatchers.anyString()
                );
    }

    @Test
    public void activateUser() {
        User user = new User();
        user.setActivationCode("real ac");
        Mockito.doReturn(user)
                .when(userRepo)
                .findByActivationCode("activate");

        boolean isUserActivated = userService.activateUser("activate");

        Assert.assertTrue(isUserActivated);
        Assert.assertNull(user.getActivationCode());
        Mockito.verify(userRepo, Mockito.times(1)).save(user);
    }

    @Test
    public void activateUserFailed() {
        boolean isUserActivated = userService.activateUser("activate");

        Assert.assertFalse(isUserActivated);
        Mockito.verify(userRepo, Mockito.times(0)).save(ArgumentMatchers.any(User.class));
    }
}