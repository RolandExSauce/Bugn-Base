import { useState, useEffect } from "react";
import Message from "../../components/admin/Message";
import type { MessageDto } from "../../types/models";
import {
  mockMessage1,
  mockMessage2,
  mockMessage3,
} from "../../types/temp/PlaceholderData";

export default function MessagesList() {
  const [messages, setMessages] = useState<MessageDto[]>([]);

  useEffect(() => {
    setMessages([mockMessage1, mockMessage2, mockMessage3]);
  }, []);

  return (
    <div className="d-flex flex-column gap-3">
      {messages.map((m) => (
        <Message key={m.messageId} message={m} />
      ))}
    </div>
  );
}
