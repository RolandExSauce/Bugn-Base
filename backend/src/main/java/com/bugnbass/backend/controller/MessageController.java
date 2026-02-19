package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.MessageRequest;
import com.bugnbass.backend.dto.MessageResponse;
import com.bugnbass.backend.service.MessageService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller responsible for user messaging functionality.
 *
 * <p>This controller provides endpoints for authenticated users to:
 * <ul>
 *     <li>Create messages (e.g., contact requests)</li>
 *     <li>View sent messages</li>
 *     <li>View inbox messages (admin replies)</li>
 *     <li>Retrieve unread message counts</li>
 *     <li>Mark messages as read</li>
 * </ul>
 *
 * <p>All endpoints require authentication with either {@code USER} or {@code ADMIN} role.
 */
@RestController
@RequestMapping("/bugnbass/api/messages")
@RequiredArgsConstructor
public class MessageController {

    /**
     * Service responsible for message operations.
     */
    private final MessageService messageService;

    /**
     * Creates a new message.
     *
     * <p>The authenticated user is automatically associated with the message.
     *
     * @param req  the message creation request payload
     * @param auth the current authentication context
     * @return the created {@link MessageResponse}
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public MessageResponse create(
            @Valid @RequestBody MessageRequest req,
            Authentication auth
    ) {
        return messageService.create(req, auth);
    }

    /**
     * Retrieves messages sent by the authenticated user.
     *
     * @param auth the current authentication context
     * @return list of sent {@link MessageResponse} objects
     */
    @GetMapping("/sent")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<MessageResponse> sent(Authentication auth) {
        return messageService.getSent(auth);
    }

    /**
     * Retrieves inbox messages for the authenticated user.
     *
     * <p>Inbox messages typically represent admin replies to user messages.
     *
     * @param auth the current authentication context
     * @return list of inbox {@link MessageResponse} objects
     */
    @GetMapping("/inbox")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<MessageResponse> inbox(Authentication auth) {
        return messageService.getInbox(auth);
    }

    /**
     * Retrieves the count of unread inbox messages for the authenticated user.
     *
     * @param auth the current authentication context
     * @return number of unread messages
     */
    @GetMapping("/inbox/unread-count")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public long inboxUnreadCount(Authentication auth) {
        return messageService.getInboxUnreadCount(auth);
    }

    /**
     * Marks a specific message as read.
     *
     * <p>This updates the read timestamp of the message.
     *
     * @param id   the message identifier
     * @param auth the current authentication context
     */
    @PatchMapping("/{id}/read")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public void markAsRead(
            @PathVariable("id") UUID id,
            Authentication auth
    ) {
        messageService.markAsRead(id, auth);
    }
}
