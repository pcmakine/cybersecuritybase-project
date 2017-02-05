package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.domain.Account;
import sec.project.domain.Message;
import sec.project.repository.AccountRepository;
import sec.project.repository.MessageRepository;
import sec.project.repository.SignupRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProfileController {

    @Autowired
    private SignupRepository signupRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MessageRepository messageRepository;

    @RequestMapping(value = "/profile/{id}", method = RequestMethod.GET)
    public String loadProfile(@PathVariable Long id, Model model) {
        System.out.println("Loading profile with id " + id);
        Account account = accountRepository.findOne(id);
        if( account == null ){
            return "redirect:/chat";
        }
        List<Message> messages = messageRepository.findByAuthor(account);
        if( messages == null ){
            messages = new ArrayList<>();
        }
        System.out.println("User " + account.getUsername() + " has " + messages.size() + " messages");

        model.addAttribute("messages", messages);
        model.addAttribute("account", account);
        return "profile";
    }
}
