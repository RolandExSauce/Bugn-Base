package com.bugnbass.backend.service;

import com.bugnbass.backend.dto.MessageRequest;
import com.bugnbass.backend.dto.MessageResponse;
import com.bugnbass.backend.model.Message;
import com.bugnbass.backend.model.User;
import com.bugnbass.backend.model.enums.MessageStatus;
import com.bugnbass.backend.repository.MessageRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsible for user messaging functionality.
 *
 * <p>This service handles:
 * <ul>
 *     <li>Creating messages</li>
 *     <li>Retrieving sent messages</li>
 *     <li>Retrieving inbox messages (admin replies)</li>
 *     <li>Marking messages as read</li>
 *     <li>Counting unread inbox messages</li>
 * </ul>
 *
 * <p>User context is derived from the Spring Security {@link Authentication} object.
 */
@Service
@RequiredArgsConstructor
public class MessageService {

    /**
     * Repository for message persistence.
     */
    private final MessageRepository messageRepository;

    /**
     * Service used to resolve authenticated users.
     */
    private final UserService userService;

    /**
     * Creates a new message associated with the authenticated user.
     *
     * @param req            the message request payload
     * @param authentication the current authentication context
     * @return the created {@link MessageResponse}
     */
    @Transactional
    public MessageResponse create(MessageRequest req, Authentication authentication) {
        User user = resolveUser(authentication);

        Message m = Message.builder()
                .name(req.name())
                .email(req.email())
                .subject(req.subject())
                .message(req.message())
                .user(user)
                .status(MessageStatus.OPEN)
                .build();

        return toResponse(messageRepository.save(m));
    }

    /**
     * Retrieves all messages sent by the authenticated user.
     *
     * @param authentication the current authentication context
     * @return list of sent {@link MessageResponse} objects
     */
    @Transactional(readOnly = true)
    public List<MessageResponse> getSent(Authentication authentication) {
        User user = resolveUser(authentication);

        return messageRepository.findAllByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Retrieves inbox messages for the authenticated user.
     *
     * <p>Inbox messages represent messages that have received
     * an administrative reply.
     *
     * @param authentication the current authentication context
     * @return list of inbox {@link MessageResponse} objects
     */
    @Transactional(readOnly = true)
    public List<MessageResponse> getInbox(Authentication authentication) {
        User user = resolveUser(authentication);

        return messageRepository
                .findAllByUserIdAndStatusOrderByRepliedAtDesc(
                        user.getId(),
                        MessageStatus.REPLIED
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    /**
     * Marks a specific message as read for the authenticated user.
     *
     * @param messageId the message identifier
     * @param auth      the authentication context
     * @throws RuntimeException if the message does not exist or does not belong to the user
     */
    @Transactional
    public void markAsRead(UUID messageId, Authentication auth) {
        User user = resolveUser(auth);

        int updated = messageRepository.markAsRead(
                messageId,
                user.getId(),
                LocalDateTime.now()
        );

        if (updated == 0) {
            throw new RuntimeException("Message not found or not allowed");
        }
    }

    /**
     * Resolves the authenticated user from the security context.
     *
     * @param authentication the authentication object
     * @return the resolved {@link User}
     * @throws UsernameNotFoundException if no user with the given email exists
     */
    private User resolveUser(Authentication authentication) {
        String email = authentication.getName();

        return userService.findCustomerByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
    }

    /**
     * Converts a {@link Message} entity into a {@link MessageResponse} DTO.
     *
     * @param m the message entity
     * @return mapped response object
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

    /**
     * Retrieves the number of unread inbox messages for the authenticated user.
     *
     * @param auth the authentication context
     * @return count of unread messages
     */
    @Transactional(readOnly = true)
    public long getInboxUnreadCount(Authentication auth) {
        User user = resolveUser(auth);

        return messageRepository.countByUserIdAndStatusAndReadAtIsNull(
                user.getId(),
                MessageStatus.REPLIED
        );
    }

}
