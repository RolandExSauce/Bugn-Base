import { apiClient } from "../api/api-client";
import type { MessageDto } from "../types/models";

export const AdminMessageService = {
  getAllMessages: () => apiClient.get<MessageDto[]>("/admin/messages"),
  sendReply: (id: string, reply: string) =>
    apiClient.post<MessageDto>(`/admin/messages/${id}/send-reply`, { reply }),
};

export default AdminMessageService;
