export default function Review() {
  return (
    <div className="review-main p-2 w-100 border rounded">
      <div className="review-username d-flex flex-row my-2">
        Username
        <span className="review-stars mx-3 d-flex flex-row">
          <img width="20px" src="/star.svg" alt="" />
          <img width="20px" src="/star.svg" alt="" />
        </span>
      </div>

      <div className="review-message">
        Lorem ipsum dolor sit amet consectetur adipisicing elit. Quam quia non
        reprehenderit culpa, olorem ea?
      </div>
    </div>
  );
}
