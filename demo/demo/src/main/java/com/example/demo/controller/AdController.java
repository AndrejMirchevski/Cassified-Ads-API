package com.example.demo.controller;

import com.example.demo.dto.AdRequestDTO;
import com.example.demo.dto.MessageRequestDTO;
import com.example.demo.entity.Ad;
import com.example.demo.entity.AuditLog;
import com.example.demo.entity.Message;
import com.example.demo.repository.AdRepository;
import com.example.demo.service.AdService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/ads")
public class AdController {

    @Autowired
    private AdService adService;

    @Autowired
    private AdRepository adRepository;

    //create an ad
    @PostMapping
    public ResponseEntity<Ad> createAd(@Valid @RequestBody AdRequestDTO dto) {
        return ResponseEntity.ok(adService.createAd(dto));
    }

    //get all of the ads
    @GetMapping
    public ResponseEntity<Page<Ad>> getAllAds(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(adRepository.browseAds(categoryId, city, minPrice, maxPrice, PageRequest.of(page, size)));
    }

    //filter by id of ad
    @GetMapping("/{id}")
    public ResponseEntity<Ad> getAdById(@PathVariable Long id) {
        return adRepository.findById(id)
                .filter(ad -> ad.getDeletedAt() == null)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    //update ad by id
    @PatchMapping("/{id}")
    public ResponseEntity<Ad> updateAd(@PathVariable Long id, @Valid @RequestBody AdRequestDTO dto) {
        return ResponseEntity.ok(adService.updateAd(id, dto));
    }

    //mark ad by id as sold
    @PatchMapping("/{id}/sold")
    public ResponseEntity<Ad> markAsSold(@PathVariable Long id) {
        return ResponseEntity.ok(adService.markAsSold(id));
    }

    //renew the Expiers_at time
    @PatchMapping("/{id}/renew")
    public ResponseEntity<Ad> renewAd(@PathVariable Long id) {
        return ResponseEntity.ok(adService.renewAd(id));
    }

    //delete ad by id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable Long id) {
        adService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    //add message to ad
    @PostMapping("/{id}/messages")
    public ResponseEntity<Message> sendMessage(@PathVariable Long id, @Valid @RequestBody MessageRequestDTO dto) {
        return ResponseEntity.ok(adService.leaveMessage(id, dto));
    }

    //audit tracking
    @GetMapping("/{id}/audit")
    public ResponseEntity<List<AuditLog>> getAdAuditLogs(@PathVariable Long id) {
        return ResponseEntity.ok(adService.getAuditLogs(id));
    }

    //get all expiring soon ads
    @GetMapping("/reports/expiring-soon")
    public ResponseEntity<List<Ad>> getExpiringSoon() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threeDaysLater = now.plusDays(3);
        return ResponseEntity.ok(adRepository.findByExpiresAtBetweenAndDeletedAtIsNull(now, threeDaysLater));
    }

    //get the total count of ads
    @GetMapping("/count")
    public ResponseEntity<Long> countAds() {
        return ResponseEntity.ok(adRepository.count());
    }
}