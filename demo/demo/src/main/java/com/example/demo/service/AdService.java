package com.example.demo.service;

import com.example.demo.dto.AdRequestDTO;
import com.example.demo.dto.MessageRequestDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class AdService {

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    public Ad createAd(AdRequestDTO dto) {
        Ad ad = new Ad();
        ad.setTitle(dto.getTitle());
        ad.setCategoryId(dto.getCategoryId());
        ad.setPriceEur(dto.getPriceEur());
        ad.setCity(dto.getCity());
        ad.setSellerName(dto.getSellerName());
        ad.setDescription(dto.getDescription());
        ad.setStatus(AdStatus.ACTIVE);
        ad.setExpiresAt(LocalDateTime.now().plusDays(30));

        Ad savedAd = adRepository.save(ad);
        auditLogRepository.save(new AuditLog(savedAd.getId(), "CREATE", "Ad created successfully."));
        return savedAd;
    }

    public Ad updateAd(Long id, AdRequestDTO dto) {
        Ad ad = getActiveOrSoldAd(id);

        //check to lock if ad is sold
        if (ad.getStatus() == AdStatus.SOLD) {
            throw new IllegalStateException("Once an ad is marked SOLD, it is locked and cannot be edited.");
        }

        ad.setTitle(dto.getTitle());
        ad.setCategoryId(dto.getCategoryId());
        ad.setPriceEur(dto.getPriceEur());
        ad.setCity(dto.getCity());
        ad.setDescription(dto.getDescription());

        auditLogRepository.save(new AuditLog(id, "UPDATE", "Ad fields updated. Price: " + dto.getPriceEur()));
        return adRepository.save(ad);
    }

    public Ad markAsSold(Long id) {
        Ad ad = getActiveOrSoldAd(id);

        if (ad.getStatus() == AdStatus.SOLD) {
            throw new IllegalStateException("Ad is already marked as SOLD.");
        }

        ad.setStatus(AdStatus.SOLD);
        auditLogRepository.save(new AuditLog(id, "STATUS_CHANGE", "Ad marked as SOLD. System locked."));
        return adRepository.save(ad);
    }

    public Ad renewAd(Long id) {
        Ad ad = getActiveOrSoldAd(id);

        //renew only if ad is active
        if (ad.getStatus() != AdStatus.ACTIVE) {
            throw new IllegalStateException("Only ACTIVE ads can be renewed.");
        }

        ad.setExpiresAt(LocalDateTime.now().plusDays(30));
        auditLogRepository.save(new AuditLog(id, "RENEW", "Ad placement extended by 30 days."));
        return adRepository.save(ad);
    }

    public void softDelete(Long id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ad not found"));

        ad.setDeletedAt(LocalDateTime.now());
        auditLogRepository.save(new AuditLog(id, "DELETE", "Ad soft-deleted."));
        adRepository.save(ad);
    }

    public Message leaveMessage(Long adId, MessageRequestDTO dto) {
        Ad ad = getActiveOrSoldAd(adId);

        Message msg = new Message();
        msg.setAdId(ad.getId());
        msg.setMessageText(dto.getText());

        auditLogRepository.save(new AuditLog(adId, "MESSAGE", "Buyer left a message context."));
        return messageRepository.save(msg);
    }

    public List<AuditLog> getAuditLogs(Long adId) {
        return auditLogRepository.findByAdIdOrderByTimestampDesc(adId);
    }

    //background job running every hour to cleanly flip expired postings
    @Scheduled(cron = "0 0 * * * *")
    public void processExpiredAds() {
        List<Ad> expired = adRepository.findByStatusAndExpiresAtBeforeAndDeletedAtIsNull(AdStatus.ACTIVE, LocalDateTime.now());
        for (Ad ad : expired) {
            ad.setStatus(AdStatus.EXPIRED);
            adRepository.save(ad);
            auditLogRepository.save(new AuditLog(ad.getId(), "EXPIRE", "System marked ad as EXPIRED."));
        }
    }

    private Ad getActiveOrSoldAd(Long id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Ad not found"));
        if (ad.getDeletedAt() != null) {
            throw new EntityNotFoundException("Ad has been deleted.");
        }
        return ad;
    }
}