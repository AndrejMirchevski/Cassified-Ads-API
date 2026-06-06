package com.example.demo.repository;

import com.example.demo.entity.Ad;
import com.example.demo.entity .AdStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdRepository extends JpaRepository<Ad, Long> {

    // filtering for getting ads by specific variables
    @Query("SELECT a FROM Ad a WHERE a.deletedAt IS NULL AND a.status = 'ACTIVE' " +
            "AND (:categoryId IS NULL OR a.categoryId = :categoryId) " +
            "AND (:city IS NULL OR LOWER(a.city) = LOWER(:city)) " +
            "AND (:minPrice IS NULL OR a.priceEur >= :minPrice) " +
            "AND (:maxPrice IS NULL OR a.priceEur <= :maxPrice)")
    Page<Ad> browseAds(@Param("categoryId") Long categoryId,
                       @Param("city") String city,
                       @Param("minPrice") Double minPrice,
                       @Param("maxPrice") Double maxPrice,
                       Pageable pageable);

    List<Ad> findByExpiresAtBetweenAndDeletedAtIsNull(LocalDateTime start, LocalDateTime end);

    List<Ad> findByStatusAndExpiresAtBeforeAndDeletedAtIsNull(AdStatus status, LocalDateTime dateTime);
}