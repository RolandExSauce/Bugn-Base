package com.bugnbass.backend.service;

import com.bugnbass.backend.dto.MessageResponse;
import com.bugnbass.backend.model.Message;
import com.bugnbass.backend.model.enums.MessageStatus;
import com.bugnbass.backend.repository.MessageRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminMessageServiceTest {

    @Mock
    MessageRepository messageRepository;

    @InjectMocks
    AdminMessageService adminMessageService;

    @Test
    void getAll_fetchesSortedDescendingByCreatedAt_andMapsToResponse() {
        Message m1 = Message.builder()
                .id(UUID.randomUUID())
                .name("Alice")
                .email("a@test.com")
                .subject("S1")
                .message("Hello")
                .createdAt(LocalDateTime.now().minusDays(1))
                .status(MessageStatus.OPEN)
                .build();

        Message m2 = Message.builder()
                .id(UUID.randomUUID())
                .name("Bob")
                .email("b@test.com")
                .subject("S2")
                .message("Hi")
                .createdAt(LocalDateTime.now())
                .status(MessageStatus.REPLIED)
                .adminReply("Reply")
                .repliedAt(LocalDateTime.now())
                .build();

        when(messageRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt")))
                .thenReturn(List.of(m2, m1));

        List<MessageResponse> result = adminMessageService.getAll();

        assertThat(result).hasSize(2);

        assertThat(result.get(0).id()).isEqualTo(m2.getId());
        assertThat(result.get(0).name()).isEqualTo("Bob");
        assertThat(result.get(0).messageStatus()).isEqualTo(MessageStatus.REPLIED);

        assertThat(result.get(1).id()).isEqualTo(m1.getId());
        assertThat(result.get(1).name()).isEqualTo("Alice");
        assertThat(result.get(1).messageStatus()).isEqualTo(MessageStatus.OPEN);

        verify(messageRepository).findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
        verifyNoMoreInteractions(messageRepository);
    }

    @Test
    void sendReply_setsReplyTimestampAndStatus_saves_andReturnsResponse() {
        UUID id = UUID.randomUUID();

        Message existing = new Message();
        existing.setId(id);
        existing.setStatus(MessageStatus.OPEN);

        when(messageRepository.findById(id)).thenReturn(Optional.of(existing));
        when(messageRepository.save(any(Message.class))).thenAnswer(inv -> inv.getArgument(0));

        MessageResponse res = adminMessageService.sendReply(id, "Thanks!");

        // persisted entity assertions (captured)
        ArgumentCaptor<Message> captor = ArgumentCaptor.forClass(Message.class);
        verify(messageRepository).save(captor.capture());

        Message saved = captor.getValue();
        assertThat(saved.getId()).isEqualTo(id);
        assertThat(saved.getAdminReply()).isEqualTo("Thanks!");
        assertThat(saved.getStatus()).isEqualTo(MessageStatus.REPLIED);
        assertThat(saved.getRepliedAt()).isNotNull();

        // response assertions
        assertThat(res.id()).isEqualTo(id);
        assertThat(res.adminReply()).isEqualTo("Thanks!");
        assertThat(res.messageStatus()).isEqualTo(MessageStatus.REPLIED);
        assertThat(res.repliedAt()).isNotNull();

        verify(messageRepository).findById(eq(id));
        verifyNoMoreInteractions(messageRepository);
    }

    @Test
    void sendReply_throwsIllegalArgumentException_whenMessageNotFound() {
        UUID id = UUID.randomUUID();
        when(messageRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminMessageService.sendReply(id, "Hi"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Message not found");

        verify(messageRepository).findById(id);
        verifyNoMoreInteractions(messageRepository);
    }
}
