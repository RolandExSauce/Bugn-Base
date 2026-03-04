import { useEffect, useMemo, useState } from "react";
import Message from "../../components/admin/Message";
import type { MessageDto } from "../../types/models";
import { AdminMessageService } from "../../services/admin.message.service";

type Filter = "OPEN" | "REPLIED";

function isReplied(m: any): boolean {
  if (typeof m?.status === "string") {
    return m.status.toLowerCase() === "replied";
  }
  return Boolean(m?.repliedAt) || Boolean(m?.adminReply);
}

export default function MessagesList() {
  const [messages, setMessages] = useState<MessageDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [filter, setFilter] = useState<Filter>("OPEN");

  const fetchMessages = async () => {
    try {
      setLoading(true);
      const data = await AdminMessageService.getAllMessages();
      setMessages(data);
      setError(null);
    } catch (err) {
      console.error("Error fetching messages:", err);
      setError("Nachrichten konnten nicht geladen werden.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchMessages();
  }, []);

  const counts = useMemo(() => {
    const replied = messages.filter((m) => isReplied(m)).length;
    const open = messages.length - replied;
    return { open, replied };
  }, [messages]);

  const filteredMessages = useMemo(() => {
    if (filter === "OPEN") return messages.filter((m) => !isReplied(m));
    return messages.filter((m) => isReplied(m));
  }, [messages, filter]);

  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center py-5">
        <div className="spinner-border text-primary" role="status" />
        <p className="mt-3 ms-3">Lade Nachrichten...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="alert alert-danger">
        {error}
        <button
          onClick={fetchMessages}
          className="btn btn-sm btn-outline-danger ms-3"
          aria-label="Erneut versuchen"
        >
          Erneut versuchen
        </button>
      </div>
    );
  }

  return (
    <div className="d-flex flex-column gap-3">
      {/* Filter Buttons */}
      <div className="d-flex align-items-center gap-2">
        <div className="btn-group btn-group-sm" role="group">
          <button
            type="button"
            className={`btn ${
              filter === "OPEN" ? "btn-primary" : "btn-outline-primary"
            }`}
            onClick={() => setFilter("OPEN")}
            aria-label="Oefnen"
          >
            Offen
            <span className="badge text-bg-light ms-1">{counts.open}</span>
          </button>

          <button
            type="button"
            className={`btn ${
              filter === "REPLIED" ? "btn-primary" : "btn-outline-primary"
            }`}
            onClick={() => setFilter("REPLIED")}
            aria-label="Replied"
          >
            Beantwortet
            <span className="badge text-bg-light ms-1">{counts.replied}</span>
          </button>
        </div>
      </div>

      {/* Liste */}
      {filteredMessages.length === 0 ? (
        <div className="alert alert-info">
          Keine Nachrichten in dieser Kategorie.
        </div>
      ) : (
        filteredMessages.map((m: any) => (
          <Message
            key={m.id}
            message={m}
            onReplied={(updated: any) =>
              setMessages((prev) =>
                prev.map((msg: any) => (msg.id === updated.id ? updated : msg)),
              )
            }
          />
        ))
      )}
    </div>
  );
}
