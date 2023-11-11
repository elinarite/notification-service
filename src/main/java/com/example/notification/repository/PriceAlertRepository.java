package com.example.notification.repository;

import com.example.notification.model.entity.Currency;
import com.example.notification.model.entity.PriceAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {
    List<PriceAlert> findAll();

    @Query("Select p from PriceAlert p where p.user.chatId = :chatId and p.currencyName = :currencyName")
    Optional<PriceAlert> findByUserIdAndCurrency(@Param("chatId") Long chatId, @Param("currencyName") Currency currencyName);

    @Query("Select p from PriceAlert p where p.user.chatId = :chatId order by p.createdAt desc")
    List<PriceAlert> findByUserIdOrderByDesc(@Param("chatId") Long chatId);
}