package com.bugnbass.backend.model;

import com.bugnbass.backend.model.enums.MessageStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

/**
 * Entity representing a message sent by a user, typically through a contact form.
 *
 * <p>A message can optionally be associated with a registered {@link User}.
 * Administrators may respond to messages, which updates the reply fields
 * and message status.
 */
@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    /**
     * Unique identifier of the message.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "message_id")
    private UUID id;

    /**
     * Name of the sender.
     */
    private String name;

    /**
     * Email address of the sender.
     */
    private String email;

    /**
     * Subject of the message.
     */
    private String subject;

    /**
     * Message content provided by the sender.
     */
    private String message;

    /**
     * Timestamp indicating when the message was created.
     * Automatically generated when the entity is persisted.
     */
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * Optional reference to a registered user who sent the message.
     * May be null if the message was submitted anonymously.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Administrative reply text.
     */
    @Column(name = "admin_reply")
    private String adminReply;

    /**
     * Timestamp indicating when the admin reply was created.
     */
    @Column(name = "replied_at")
    private LocalDateTime repliedAt;

    /**
     * Current status of the message.
     * Defaults to {@link MessageStatus#OPEN}.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageStatus status = MessageStatus.OPEN;

    /**
     * Timestamp indicating when the message was read by the recipient.
     * Used for inbox functionality.
     */
    @Column(name = "read_at")
    private LocalDateTime readAt;

}
