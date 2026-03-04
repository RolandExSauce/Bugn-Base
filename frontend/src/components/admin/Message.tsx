import { useEffect, useRef, useState } from "react";
import type { MessageDto } from "../../types/models";
import { AdminMessageService } from "../../services/admin.message.service";

type Props = {
  message: MessageDto;
  onReplied?: (updated: MessageDto) => void;
};

export default function Message({ message, onReplied }: Props) {
  const [reply, setReply] = useState("");
  const [sending, setSending] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const textareaRef = useRef<HTMLTextAreaElement | null>(null);

  const isReplied =
    String((message as any).messageStatus ?? "").toUpperCase() === "REPLIED" ||
    Boolean((message as any).adminReply) ||
    Boolean((message as any).repliedAt);

  const formatDateTime = (iso?: string | null) => {
    if (!iso) return "";
    try {
      return new Date(iso).toLocaleString("de-DE", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
        hour: "2-digit",
        minute: "2-digit",
      });
    } catch {
      return String(iso);
    }
  };

  const resizeTextarea = () => {
    const el = textareaRef.current;
    if (!el) return;
    el.style.height = "auto";
    el.style.height = el.scrollHeight + "px";
  };

  useEffect(() => {
    resizeTextarea();
  }, [reply]);

  const handleSendReply = async () => {
    const text = reply.trim();
    if (!text || sending) return;

    try {
      setSending(true);
      setError(null);

      const updated = await AdminMessageService.sendReply(message.id, text);

      onReplied?.(updated);
      setReply("");
      requestAnimationFrame(resizeTextarea);
    } catch (e) {
      console.error("Error sending reply:", e);
      setError("Antwort konnte nicht gesendet werden.");
    } finally {
      setSending(false);
    }
  };

  return (
    <div className="card mb-3">
      <div className="card-body">
        <div className="d-flex justify-content-between align-items-start">
          <div className="fw-bold">
            {message.subject || "Ohne Betreff"}
            <span
              className={`badge ms-2 ${
                isReplied ? "text-bg-success" : "text-bg-warning"
              }`}
            >
              {isReplied ? "Beantwortet" : "Offen"}
            </span>
          </div>

          <small className="text-muted">
            {formatDateTime(message.createdAt)}
          </small>
        </div>
        <div className="small text-muted mt-1">
          {message.name} · {message.email}
        </div>
        <div className="mt-2 bg-light rounded p-2">
          <div style={{ whiteSpace: "pre-wrap" }}>{message.message}</div>
        </div>
        {isReplied && (
          <>
            <hr className="my-3" />
            <div>
              <div className="fw-bold">Antwort</div>
              <div className="mt-1" style={{ whiteSpace: "pre-wrap" }}>
                {(message as any).adminReply}
              </div>
              {(message as any).repliedAt && (
                <small className="text-muted d-block mt-2">
                  {formatDateTime((message as any).repliedAt)}
                </small>
              )}
            </div>
          </>
        )}

        {!isReplied && (
          <>
            <hr className="my-3" />

            <textarea
              ref={textareaRef}
              className="form-control"
              rows={1}
              placeholder="Antwort schreiben…"
              value={reply}
              onChange={(e) => setReply(e.target.value)}
              disabled={sending}
              style={{
                resize: "none",
                overflow: "hidden",
              }}
            />

            <div className="d-flex justify-content-between align-items-center mt-2">
              {error ? (
                <small className="text-danger">{error}</small>
              ) : (
                <span />
              )}

              <button
                type="button"
                className="btn btn-sm btn-primary"
                onClick={handleSendReply}
                disabled={sending || !reply.trim()}
                aria-label="Send reply"
              >
                {sending ? "Sende…" : "Antworten"}
              </button>
            </div>
          </>
        )}
      </div>
    </div>
  );
}
