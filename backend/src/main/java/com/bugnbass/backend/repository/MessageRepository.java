package com.bugnbass.backend.repository;

import com.bugnbass.backend.model.Message;
import com.bugnbass.backend.model.enums.MessageStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository interface for {@link Message} persistence operations.
 *
 * <p>Provides query methods for retrieving user messages, inbox messages,
 * unread counts, and updating read status.
 */
public interface MessageRepository extends JpaRepository<Message, UUID> {

    /**
     * Retrieves all messages belonging to a specific user,
     * ordered by creation timestamp descending.
     *
     * @param userId the user identifier
     * @return list of messages
     */
    List<Message> findAllByUserIdOrderByCreatedAtDesc(UUID userId);

    /**
     * Retrieves messages for a specific user filtered by status,
     * ordered by reply timestamp descending.
     *
     * @param userId the user identifier
     * @param status the message status
     * @return list of messages
     */
    List<Message> findAllByUserIdAndStatusOrderByRepliedAtDesc(
            UUID userId,
            MessageStatus status
    );

    /**
     * Marks a message as read by setting the {@code readAt} timestamp.
     *
     * <p>The update will only be applied if:
     * <ul>
     *     <li>The message belongs to the specified user</li>
     *     <li>The message has not already been marked as read</li>
     * </ul>
     *
     * @param messageId the message identifier
     * @param userId    the user identifier
     * @param now       the timestamp to set as read time
     * @return number of affected rows (0 or 1)
     */
    @Modifying
    @Transactional
    @Query(
            """
                update Message m
                set m.readAt = :now
                where m.id = :messageId
                and m.user.id = :userId
                and m.readAt is null
            """
    )
    int markAsRead(
            @Param("messageId") UUID messageId,
            @Param("userId") UUID userId,
            @Param("now") LocalDateTime now
    );

    /**
     * Counts unread messages for a user with a specific status.
     *
     * <p>Typically used for inbox badge counters.
     *
     * @param userId the user identifier
     * @param status the message status
     * @return number of unread messages
     */
    long countByUserIdAndStatusAndReadAtIsNull(
            UUID userId,
            MessageStatus status
    );

}
