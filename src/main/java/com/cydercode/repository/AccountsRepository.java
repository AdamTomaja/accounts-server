package com.cydercode.repository;

import com.cydercode.model.Account;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface AccountsRepository extends CrudRepository<Account, Long> {

    int countByUsernameIgnoreCase(String username);
    int countByEmailIgnoreCase(String email);

    Optional<Account> findByEmailVerificationToken(String emailVerificationToken);
}
