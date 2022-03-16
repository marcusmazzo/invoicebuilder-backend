package com.invoice.repository;

import com.invoice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsernamePasswordRepository extends JpaRepository<User, Long> {

    User findByUsername(String s);
}
