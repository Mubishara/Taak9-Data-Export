package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.entity.Email;

public interface EmailRepository extends JpaRepository<Email, Long> {
}
