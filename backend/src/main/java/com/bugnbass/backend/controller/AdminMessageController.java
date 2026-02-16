package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.MessageResponse;
import com.bugnbass.backend.dto.ReplyMessageRequest;
import com.bugnbass.backend.service.AdminMessageService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for administrative message management.
 *
 * <p>This controller provides endpoints for administrators to:
 * <ul>
 *     <li>Retrieve all user messages</li>
 *     <li>Send replies to user messages</li>
 * </ul>
 *
 * <p>All endpoints require the user to have the {@code ADMIN} role.
 */
@RestController
@RequestMapping("/bugnbass/api/admin/messages")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminMessageController {

    /**
     * Service responsible for administrative message operations.
     */
    private final AdminMessageService adminMessageService;

    /**
     * Retrieves all messages in the system.
     *
     * <p>This endpoint returns both replied and unreplied messages
     * for administrative overview and processing.
     *
     * @return a list of {@link MessageResponse} objects representing all messages
     */
    @GetMapping
    public List<MessageResponse> getAll() {
        return adminMessageService.getAll();
    }

    /**
     * Sends an administrative reply to a specific message.
     *
     * <p>The message is identified by its unique UUID. After a successful reply:
     * <ul>
     *     <li>The reply text is stored</li>
     *     <li>The message status is updated (e.g., to REPLIED)</li>
     *     <li>A response object containing updated data is returned</li>
     * </ul>
     *
     * @param id  the unique identifier of the message to reply to
     * @param req the request payload containing the reply text
     * @return the updated {@link MessageResponse} containing reply information
     */
    @PostMapping("/{id}/send-reply")
    public MessageResponse sendReply(
            @PathVariable("id") UUID id,
            @Valid @RequestBody ReplyMessageRequest req
    ) {
        return adminMessageService.sendReply(id, req.reply());
    }
}
