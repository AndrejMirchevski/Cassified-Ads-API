package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "audit_logs")
public class AuditLog {
    // Getters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long adId;
    private String action;
    private String details;
    private final LocalDateTime timestamp = LocalDateTime.now();

    public AuditLog() {}
    public AuditLog(Long adId, String action, String details) {
        this.adId = adId;
        this.action = action;
        this.details = details;
    }

}