package sec.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sec.project.domain.Account;
import sec.project.domain.Message;
import sec.project.repository.MessageRepository;

import java.util.Date;

/**
 * Created by pcmakine on 05-Feb-17.
 */
@Service
public class MessageService {
    @Autowired
    MessageRepository messageRepository;

    public void postMessage(Account account, String content){
        Message message = new Message();
        message.setAuthor(account);
        message.setContent(content);
        message.setPostedDate(new Date());
        messageRepository.save(message);
    }
}
