package sec.project.repository;

/**
 * Created by pcmakine on 03-Feb-17.
 */

import org.springframework.data.jpa.repository.JpaRepository;
import sec.project.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Account findByUsername(String username);
    Account findByEmail(String email);
}
