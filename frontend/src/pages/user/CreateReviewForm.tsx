import { useState } from "react";
import ReviewService from "../../services/review.service";

type Props = {
  productId: number;
  onCreated?: () => void;
  onCancel?: () => void;
};

export default function CreateReviewForm({
  productId,
  onCreated,
  onCancel,
}: Props) {
  const [rating, setRating] = useState<number>(5);
  const [hoverRating, setHoverRating] = useState<number | null>(null);
  const [comment, setComment] = useState<string>("");
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const activeRating = hoverRating ?? rating;

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setSaving(true);
    setError(null);

    try {
      await ReviewService.create({
        productId,
        rating,
        comment,
      });

      setComment("");
      setRating(5);

      onCreated?.();
    } catch (err: any) {
      const msg =
        err?.response?.data?.message ??
        err?.response?.data ??
        err?.message ??
        "Review konnte nicht erstellt werden";

      setError(String(msg));
    } finally {
      setSaving(false);
    }
  };

  return (
    <form
      onSubmit={handleSubmit}
      className="border rounded p-3 d-flex flex-column gap-3"
    >
      <div className="fw-bold">Review schreiben</div>

      {/* ⭐ Sterne */}
      <div className="d-flex align-items-center gap-2">
        <div
          className="d-flex"
          onMouseLeave={() => setHoverRating(null)}
          role="radiogroup"
        >
          {Array.from({ length: 5 }).map((_, i) => {
            const value = i + 1;
            const filled = value <= activeRating;

            return (
              <button
                key={value}
                type="button"
                disabled={saving}
                onMouseEnter={() => setHoverRating(value)}
                onClick={() => setRating(value)}
                aria-label={`${value} Sterne`}
                className="border-0 bg-transparent p-0"
                style={{
                  fontSize: "30px",
                  cursor: saving ? "not-allowed" : "pointer",
                  color: filled ? "#ffc107" : "#e4e5e9",
                  transition: "color 0.15s ease, transform 0.1s ease",
                  lineHeight: 1,
                }}
                onMouseDown={(e) => (e.currentTarget.style.transform = "scale(0.9)")}
                onMouseUp={(e) => (e.currentTarget.style.transform = "scale(1)")}
              >
                {filled ? "★" : "☆"}
              </button>
            );
          })}
        </div>

        <span className="text-muted small">{rating}/5</span>
      </div>

      {/* Kommentar */}
      <div>
        <label className="form-label m-0">Kommentar (optional)</label>
        <textarea
          className="form-control"
          rows={3}
          value={comment}
          disabled={saving}
          onChange={(e) => setComment(e.target.value)}
          placeholder="Dein Kommentar..."
        />
      </div>

      {error && <div className="text-danger">{error}</div>}

      {/* Buttons */}
      <div className="d-flex justify-content-end gap-2">
        {onCancel && (
          <button
            type="button"
            className="btn btn-outline-secondary"
            onClick={onCancel}
            disabled={saving}
          >
            Abbrechen
          </button>
        )}

        <button type="submit" className="btn btn-primary" disabled={saving}>
          Speichern
        </button>
      </div>
    </form>
  );
}
