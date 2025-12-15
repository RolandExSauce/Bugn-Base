package com.bugnbass.backend.model;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "message_id")
    private UUID id;

    private String name;

    private String email;

    private String subject;

    private String message;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

}
