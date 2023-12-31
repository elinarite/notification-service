package com.example.notification.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

//todo @Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "price_alerts")
public class PriceAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "currency_name", nullable = false, length = 10)
    private Currency currencyName;

    @Column(name = "min_threshold", precision = 15, scale = 2)
    private BigDecimal minThreshold;

    @Column(name = "max_threshold", precision = 15, scale = 2)
    private BigDecimal maxThreshold;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private Timestamp updateAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PriceAlert that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(currencyName, that.currencyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, currencyName);
    }

    @Override
    public String toString() {
        return "Notification{" +
               "id=" + id +
               ", user=" + user +
               ", currencyName='" + currencyName + '\'' +
               ", minThreshold=" + minThreshold +
               ", maxThreshold=" + maxThreshold +
               ", createdAt=" + createdAt +
               ", updateAt=" + updateAt +
               '}';
    }
}