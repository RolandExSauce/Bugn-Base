import Carousel from "../components/product/Carousel";
import Review from "../components/product/Review";

export default function Product() {
  return (
    <div className="product-main">
      <div className="product-main-top">
        <div className="product-main-top-left">
          <Carousel imgUrls={["/g-1.jpg", "/g-2.jpg", "/g-3.jpg"]} />
        </div>
        <div className="product-main-top-right">
          <div className="product-main-top-right-name">Product name</div>
          <div className="product-main-top-right-description">
            Lorem ipsum dolor sit, amet consectetur adipisicing elit. Enim
            dolor, doloribus fuga temporibus aspernatur odio rerum error labore
            adipisci magni pariatur ex facilis excepturi dicta consectetur
            quibusdam delectus suscipit eligendi!
          </div>
          <div className="product-main-top-right-price">Price</div>
          <div className="product-main-top-right-brand">Brand</div>
          <div className="product-main-top-right-stock">Stock status</div>

          <div className="product-main-top-right-shipping">Shipping status</div>
          <div className="product-main-top-right-buttons">
            <input
              type="number"
              id="rating"
              name="rating"
              min="1"
              max="5"
              step="1"
              placeholder="1–5"
              required
            />
            <button>In den Warenkorb</button>
          </div>
        </div>
      </div>
      <div className="product-main-bottom">
        <span style={{ fontWeight: "bold", fontSize: "2REM" }}>
          Rezensionen
        </span>
        {Array.from({ length: 10 }).map((_, i) => (
          <Review key={i} />
        ))}
      </div>
    </div>
  );
}
