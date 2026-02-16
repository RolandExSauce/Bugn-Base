import { useEffect, useMemo, useState } from "react";
import type { MessageDto } from "../../types/models";
import MessageService from "../../services/message.service";

type Tab = "inbox" | "sent";

export default function MyMessagesPage() {
  const [tab, setTab] = useState<Tab>("inbox");
  const [messages, setMessages] = useState<MessageDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [selected, setSelected] = useState<MessageDto | null>(null);

  const fetchMessages = async (activeTab: Tab) => {
    try {
      setLoading(true);
      setError(null);

      const data =
        activeTab === "inbox"
          ? await MessageService.getInbox()
          : await MessageService.getSent();

      const sorted = [...data];

        if (activeTab === "inbox") {
          sorted.sort(
              (a, b) => new Date(b.repliedAt ?? 0).getTime() - new Date(a.repliedAt ?? 0).getTime()
          );

          sorted.sort((a, b) => Number(!!a.readAt) - Number(!!b.readAt));
        } else {
          sorted.sort(
              (a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
          );
        }

        setMessages(sorted);

    } catch (e) {
      console.error("Error fetching messages:", e);
      setError("Nachrichten konnten nicht geladen werden.");
      setMessages([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    void fetchMessages(tab);
  }, [tab]);

  const unreadCount = useMemo(() => {
    if (tab !== "inbox") return 0;
    return messages.filter((m) => !m.readAt).length;
  }, [messages, tab]);

  const formatDateTime = (iso: string | null | undefined) => {
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

  const openModal = async (m: MessageDto) => {
    setSelected(m);
    if (tab === "inbox" && !m.readAt) {
      try {
        await MessageService.markRead(m.id);

        const nowIso = new Date().toISOString();
        setMessages((prev) =>
          prev.map((x) => (x.id === m.id ? { ...x, readAt: nowIso } : x))
        );
        setSelected((prev) => (prev?.id === m.id ? { ...prev, readAt: nowIso } : prev));
      } catch (e) {
        console.error("Error marking message as read:", e);
      }
    }
  };

  const closeModal = () => setSelected(null);

  return (
    <div className="container py-4">
      <div className="d-flex justify-content-between align-items-center mb-3">
        <h1 className="mb-4">Meine Nachrichten</h1>

        <div className="btn-group">
          <button
            type="button"
            className={`btn btn-sm ${
              tab === "inbox" ? "btn-primary" : "btn-outline-primary"
            }`}
            onClick={() => setTab("inbox")}
          >
            Posteingang
            {tab === "inbox" && (
              <span className="badge text-bg-light ms-1">{unreadCount}</span>
            )}
          </button>

          <button
            type="button"
            className={`btn btn-sm ${
              tab === "sent" ? "btn-primary" : "btn-outline-primary"
            }`}
            onClick={() => setTab("sent")}
          >
            Gesendet
          </button>
        </div>
      </div>

      {loading && (
        <div className="d-flex justify-content-center align-items-center py-5">
          <div className="spinner-border text-primary" role="status">
            <span className="visually-hidden">Loading...</span>
          </div>
          <p className="mt-3 ms-3">Lade Nachrichten...</p>
        </div>
      )}

      {!loading && error && (
        <div className="alert alert-danger">
          {error}
          <button
            onClick={() => fetchMessages(tab)}
            className="btn btn-sm btn-outline-danger ms-3"
          >
            Erneut versuchen
          </button>
        </div>
      )}

      {!loading && !error && messages.length === 0 && (
        <div className="alert alert-info">
          {tab === "inbox"
            ? "Keine Antworten im Posteingang."
            : "Noch keine gesendeten Nachrichten."}
        </div>
      )}

      {!loading &&
        !error &&
        messages.map((m) => {
          const isUnread = tab === "inbox" && !m.readAt;

          return (
            <div
              key={m.id}
              className={`card mb-3 ${isUnread ? "border-primary" : ""}`}
              role="button"
              style={{ cursor: "pointer" }}
              onClick={() => void openModal(m)}
            >
              <div className="card-body">
                <div className="d-flex justify-content-between align-items-start">
                  <div className="fw-bold">
                    {m.subject || "Ohne Betreff"}
                    {isUnread && (
                      <span className="badge text-bg-primary ms-2">Neu</span>
                    )}
                  </div>
                  <small className="text-muted">{formatDateTime(m.createdAt)}</small>
                </div>

                <p className="mt-2 mb-0 text-truncate">{m.message}</p>

                {tab === "inbox" && m.adminReply && (
                  <>
                    <hr />
                    <small className="text-muted">Antwort vorhanden</small>
                  </>
                )}
              </div>
            </div>
          );
        })}

      {selected && (
        <>
          <div
            className="modal-backdrop fade show"
            onClick={closeModal}
          />

          <div
            className="modal fade show d-block"
            tabIndex={-1}
            role="dialog"
            aria-modal="true"
          >
            <div className="modal-dialog modal-lg modal-dialog-centered">
              <div className="modal-content">
                <div className="modal-header">
                  <div>
                    <h5 className="modal-title mb-0">
                      {selected.subject || "Ohne Betreff"}
                    </h5>
                    <small className="text-muted">
                      {formatDateTime(selected.createdAt)}
                      {tab === "inbox" && selected.readAt && (
                        <> · gelesen: {formatDateTime(selected.readAt)}</>
                      )}
                    </small>
                  </div>

                  <button
                    type="button"
                    className="btn-close"
                    aria-label="Close"
                    onClick={closeModal}
                  />
                </div>

                <div className="modal-body">
                  <div className="mb-3">
                    <div className="fw-bold">Nachricht</div>
                    <div className="mt-1" style={{ whiteSpace: "pre-wrap" }}>
                      {selected.message}
                    </div>
                  </div>

                  <hr />

                  {selected.adminReply ? (
                    <div>
                      <div className="fw-bold">Antwort</div>
                      <div className="mt-1" style={{ whiteSpace: "pre-wrap" }}>
                        {selected.adminReply}
                      </div>
                      {selected.repliedAt && (
                        <small className="text-muted d-block mt-2">
                          {formatDateTime(selected.repliedAt)}
                        </small>
                      )}
                    </div>
                  ) : (
                    <div className="text-muted">Noch keine Antwort.</div>
                  )}
                </div>

                <div className="modal-footer">
                  <button className="btn btn-outline-secondary" onClick={closeModal}>
                    Schließen
                  </button>
                </div>
              </div>
            </div>
          </div>
        </>
      )}
    </div>
  );
}
