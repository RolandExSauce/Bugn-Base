import type { Review } from "../../types/models";

export default function Review({ review }: { review: Review }) {
  const date = new Date(review.createdAt);

  return (
    <div className="review-main p-3 w-100 border rounded">
      <div className="review-username d-flex flex-row align-items-center my-2">
        <span className="fw-semibold">{review.userName}</span>

        <span className="review-stars mx-3 d-flex flex-row">
          {Array.from({ length: review.rating }).map((_, i) => (
            <img key={i} width="20px" src="/star.svg" alt="star" />
          ))}
        </span>

        <small className="text-muted ms-auto">
          {isNaN(date.getTime())
            ? review.createdAt
            : date.toLocaleDateString()}
        </small>
      </div>

      <div className="review-message">
        {review.comment || (
          <span className="text-muted fst-italic">Kein Kommentar</span>
        )}
      </div>
    </div>
  );
}
