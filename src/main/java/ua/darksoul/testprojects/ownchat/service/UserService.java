package ua.darksoul.testprojects.ownchat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import ua.darksoul.testprojects.ownchat.domain.Role;
import ua.darksoul.testprojects.ownchat.domain.User;
import ua.darksoul.testprojects.ownchat.exception.MailSendingException;
import ua.darksoul.testprojects.ownchat.repo.UserRepo;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${hostname:localhost:8080}")
    private String hostname;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

    public boolean addUser(User user){
        User userFromDB = userRepo.findByUsername(user.getUsername());

        if(userFromDB != null){
            return false;
        }

        user.setActive(false);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        sendMessageWithAC(user);

        userRepo.save(user);

        return true;
    }

    private void sendMessageWithAC(User user) throws MailSendingException{
        if(!StringUtils.isEmpty(user.getEmail())){
            String message = String.format(
                    "Hello, %s! \n" +
                            "Welcome to OwnChat. Please visit next link to activate your account: http://%s/activate/%s",
                    user.getUsername(),
                    hostname,
                    user.getActivationCode()
            );

            mailSender.send(user.getEmail(),"OwnChat activation code", message);
            return;
        }

        throw new MailSendingException("Your email is incorrect or empty.");
    }

    public boolean activateUser(String code) {
        User user = userRepo.findByActivationCode(code);

        if(user == null){
            return false;
        }

        user.setActivationCode(null);
        user.setActive(true);
        userRepo.save(user);

        return true;
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void saveUser(User user, String username, Map<String, String> form) {
        user.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if(roles.contains(key)){
                user.getRoles().add(Role.valueOf(key));
            }
        }

        if(form.containsKey("isActive") && "on".equals(form.get("isActive"))) {
            user.setActive(true);
            user.setActivationCode(null);
        } else {
            user.setActive(false);
            user.setActivationCode(UUID.randomUUID().toString());
        }

        userRepo.save(user);
    }

    public void updateProfile(Model model, User user, String username, String password, String email) {
        String userEmail = user.getEmail();

        boolean isEmailChanged = (email != null && !email.equals(userEmail)) ||
                (userEmail != null && !userEmail.equals(email));

        if(isEmailChanged && !StringUtils.isEmpty(email)){
            user.setEmail(email);
            user.setActivationCode(UUID.randomUUID().toString());
            sendMessageWithAC(user);
        }

        if(!StringUtils.isEmpty(username)){
            User userFromDB = userRepo.findByUsername(username);

            if(userFromDB == null){
                user.setUsername(username);
            } else {
                model.addAttribute("messageType", "danger");
                model.addAttribute("message", "User with this username already exists.");
            }
        }

        if(!StringUtils.isEmpty(password)){
            user.setPassword(passwordEncoder.encode(password));
        }

        userRepo.save(user);
    }

    public void subscribe(User currentUser, User user) {
        user.getSubscribers().add(currentUser);

        userRepo.save(user);
    }

    public void unsubscribe(User currentUser, User user) {
        user.getSubscribers().remove(currentUser);

        userRepo.save(user);
    }
}
