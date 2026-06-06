package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ads")
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private Long categoryId;
    private Double priceEur;
    private String city;
    private String sellerName;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    private AdStatus status;

    private LocalDateTime expiresAt;
    private LocalDateTime deletedAt;

    public Ad() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public Double getPriceEur() { return priceEur; }
    public void setPriceEur(Double priceEur) { this.priceEur = priceEur; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getSellerName() { return sellerName; }
    public void setSellerName(String sellerName) { this.sellerName = sellerName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public AdStatus getStatus() { return status; }
    public void setStatus(AdStatus status) { this.status = status; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    public LocalDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }
}