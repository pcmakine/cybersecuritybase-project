package sec.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import sec.project.config.CustomUserDetailsService;
import sec.project.domain.Account;
import sec.project.domain.Signup;
import sec.project.repository.AccountRepository;
import sec.project.repository.SignupRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SignupController {

    @Autowired
    private SignupRepository signupRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String loadForm() {
        return "signup";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String submitForm(@RequestParam String username, @RequestParam String password,
                             @RequestParam String email, Model model, HttpServletRequest request,
                             HttpSession session) {
        List<String> errors = new ArrayList<>();
        Account existingAccount = accountRepository.findByUsername(username);
        if( existingAccount != null){
            errors.add("Username exists already!");
            model.addAttribute("errors", errors);
            System.out.println("Username exists!");
            return "signup";
        }

        if( emailExists(email) ){
            errors.add("Email exists already!");
            model.addAttribute("errors", errors);
            System.out.println("Username exists!");
            return "signup";
        }

        Account account = new Account();
        account.setUsername(username);
        account.setPassword(passwordEncoder.encode(password));
        account.setEmail(email);
        account = accountRepository.save(account);
        try {
            request.login(username, password);
        } catch (ServletException e) {
            errors.add("Exception in logging in!");
            e.printStackTrace();
            return "signup";
        }
        System.out.println("Adding account id: " + account.getId() + " to the model");
        session.setAttribute("accountId", String.valueOf(account.getId()));
        return "redirect:/chat";
    }

    private boolean emailExists(String email){

        String query = "select * from ACCOUNT where email = '" + email + "'";
        List rows = jdbcTemplate.queryForList(query);
        System.out.println("Executed query " + query);
        System.out.println("Got " + rows.size() + " results");
        return (rows.size() > 0);

        //Below solution would work a bit better
        /* Account account = accountRepository.findByEmail(email);
        return (account != null);*/
    }

}
