package com.zorvyn.finance.repository;

import com.zorvyn.finance.entity.User;
import com.zorvyn.finance.enums.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameAndDeletedFalse(String username);

    Optional<User> findByEmailAndDeletedFalse(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<User> findAllByDeletedFalse();

    List<User> findAllByStatusAndDeletedFalse(UserStatus status);
}
