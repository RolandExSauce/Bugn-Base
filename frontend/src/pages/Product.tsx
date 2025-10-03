import Carousel from "../components/product/Carousel";
import Review from "../components/product/Review";

export default function Product() {
  return (
    <div className="product-main mt-5 flex-column row-gap-5">
      <div className="product-main-top d-flex flex-row column-gap-5 w-100">
        <div className="product-main-top-left w-50">
          <Carousel imgUrls={["/g-1.jpg", "/g-2.jpg", "/g-3.jpg"]} />
        </div>
        <div className="product-main-top-right d-flex flex-column row-gap-3 w-50">
          <div className="product-main-top-right-name h1">Product name</div>
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
      <div className="product-main-bottom d-flex flex-column row-gap-3">
        <span className="product-main-bottom-title mt-4 fw-bold fs-2 border-top">
          Rezensionen
        </span>
        {Array.from({ length: 10 }).map((_, i) => (
          <Review key={i} />
        ))}
      </div>
    </div>
  );
}
