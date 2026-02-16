package com.bugnbass.backend.service;

import com.bugnbass.backend.dto.MessageResponse;
import com.bugnbass.backend.model.Message;
import com.bugnbass.backend.model.enums.MessageStatus;
import com.bugnbass.backend.repository.MessageRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsible for administrative message operations.
 *
 * <p>This service provides functionality for administrators to:
 * <ul>
 *     <li>Retrieve all user messages</li>
 *     <li>Send replies to messages</li>
 *     <li>Update message status and metadata</li>
 * </ul>
 *
 * <p>All operations are executed within transactional boundaries
 * to ensure data consistency.
 */
@Service
@RequiredArgsConstructor
public class AdminMessageService {

    /**
     * Repository for accessing message persistence.
     */
    private final MessageRepository messageRepository;

    /**
     * Retrieves all messages sorted by creation date in descending order.
     *
     * <p>This method is read-only and does not modify any database state.
     *
     * @return a list of {@link MessageResponse} objects representing all messages
     */
    @Transactional(readOnly = true)
    public List<MessageResponse> getAll() {
        return messageRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Sends an administrative reply to a specific message.
     *
     * <p>The method performs the following steps:
     * <ul>
     *     <li>Loads the message by its unique identifier</li>
     *     <li>Sets the admin reply text</li>
     *     <li>Updates the reply timestamp</li>
     *     <li>Changes the message status to {@link MessageStatus#REPLIED}</li>
     *     <li>Persists the updated entity</li>
     * </ul>
     *
     * @param id    the unique identifier of the message
     * @param reply the reply text provided by the administrator
     * @return the updated {@link MessageResponse}
     * @throws IllegalArgumentException if the message does not exist
     */
    @Transactional
    public MessageResponse sendReply(UUID id, String reply) {
        Message m = messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Message not found: " + id));

        m.setAdminReply(reply);
        m.setRepliedAt(LocalDateTime.now());
        m.setStatus(MessageStatus.REPLIED);

        return toResponse(messageRepository.save(m));
    }

    /**
     * Converts a {@link Message} entity into a {@link MessageResponse} DTO.
     *
     * @param m the message entity
     * @return the mapped response object
     */
    private MessageResponse toResponse(Message m) {
        return new MessageResponse(
                m.getId(),
                m.getName(),
                m.getEmail(),
                m.getSubject(),
                m.getMessage(),
                m.getCreatedAt(),
                m.getAdminReply(),
                m.getRepliedAt(),
                m.getStatus(),
                m.getReadAt()
        );
    }
}
