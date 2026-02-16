package com.bugnbass.backend.model.enums;

/**
 * Enumeration representing the lifecycle status of a message.
 *
 * <p>OPEN indicates that the message has been created but not yet
 * responded to by an administrator.
 *
 * <p>REPLIED indicates that an administrative response has been
 * provided and the message is considered processed.
 */
public enum MessageStatus {
    OPEN,
    REPLIED
}
