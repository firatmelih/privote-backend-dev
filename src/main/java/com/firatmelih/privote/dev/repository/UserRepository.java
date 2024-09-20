package com.firatmelih.privote.dev.repository;

import com.firatmelih.privote.dev.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);

    User findUserById(int id);

    List<User> findByUsernameIn(List<String> usernames);
    // Optional<User> findById(Integer id);
}
