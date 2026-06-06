package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long adId;

    @Column(length = 2000)
    private String messageText;

    private LocalDateTime createdAt = LocalDateTime.now();

    public void setId(Long id) { this.id = id; }

    public void setAdId(Long adId) { this.adId = adId; }

    public void setMessageText(String messageText) { this.messageText = messageText; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}