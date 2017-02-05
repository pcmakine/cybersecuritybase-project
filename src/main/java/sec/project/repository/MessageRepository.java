package sec.project.repository;

/**
 * Created by pcmakine on 03-Feb-17.
 */

import org.springframework.data.jpa.repository.JpaRepository;
import sec.project.domain.Account;
import sec.project.domain.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByAuthor(Account account);
}
