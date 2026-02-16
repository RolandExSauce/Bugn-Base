import { apiClient } from "../api/api-client";
import type { MessageDto } from "../types/models";

export type CreateMessageRequest = {
  name: string;
  email: string;
  subject?: string;
  message: string;
};

class MessageService {

  async createMessage(data: CreateMessageRequest): Promise<MessageDto> {
    return apiClient.post<MessageDto>("/messages", {
      ...data,
      subject: data.subject ?? "",
    });
  }

  async getInbox(): Promise<MessageDto[]> {
    return apiClient.get<MessageDto[]>("/messages/inbox");
  }

  async getSent(): Promise<MessageDto[]> {
    return apiClient.get<MessageDto[]>("/messages/sent");
  }

  async markRead(id: string): Promise<void> {
    await apiClient.patch(`/messages/${id}/read`);
  }

  async getInboxUnreadCount(): Promise<number> {
  return apiClient.get<number>("/messages/inbox/unread-count");
}

}

export default new MessageService();

