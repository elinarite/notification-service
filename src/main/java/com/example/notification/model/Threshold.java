package com.example.notification.model;

import com.example.notification.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Threshold implements Comparable<Threshold> {

    private BigDecimal value;
    private User user;

    public Threshold(BigDecimal value) {
        this.value = value;
    }


    @Override
    public int compareTo(Threshold threshold) {
        return value.compareTo(threshold.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Threshold threshold)) return false;
        return Objects.equals(value, threshold.value) && Objects.equals(user, threshold.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, user);
    }
}