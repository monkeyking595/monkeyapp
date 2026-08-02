package com.thaimei.myapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.thaimei.myapp.model.ProcessWebhook;

@Repository
public interface  ProcessWebhookRepo  extends JpaRepository<ProcessWebhook, String> {
     // existsById(String) comes free from JpaRepository, so no need to declare it again.
}
