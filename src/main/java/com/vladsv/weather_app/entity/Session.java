package com.vladsv.weather_app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "sessions")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    @Id
    private UUID id;

    @Column(name="expires_at", nullable = false)
    private LocalDateTime expiryAt;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}
