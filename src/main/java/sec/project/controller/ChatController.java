package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import sec.project.domain.Account;
import sec.project.domain.Message;
import sec.project.repository.AccountRepository;
import sec.project.repository.MessageRepository;
import sec.project.repository.SignupRepository;
import sec.project.service.MessageService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class ChatController {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private MessageService messageService;
    @Autowired
    private SignupRepository signupRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(value = "/chat", method = RequestMethod.GET)
    public String loadChat(Model model) {
        List<Message> messages = messageRepository.findAll();
        messages.sort(Comparator.comparing(Message::getPostedDate).reversed());
        model.addAttribute("messages", messages);
        return "chat";
    }

    @RequestMapping(value = "/chat", method = RequestMethod.POST)
    public String post(@RequestParam String message, Principal loggedInUser, RedirectAttributes redirectAttrs) {
        List errors = new ArrayList();
        String username = loggedInUser.getName();
        Account account = accountRepository.findByUsername(username);

        if(message == null || message.length() == 0){
            errors.add("Message has no content");
            redirectAttrs.addFlashAttribute("errors", errors);
            return "redirect:/chat";
        }

        if( account == null ){
            errors.add("User not found in the system");
            redirectAttrs.addFlashAttribute("errors", errors);
            return "redirect:/chat";
        }
        messageService.postMessage(account, message);

        return "redirect:/chat";
    }

}
