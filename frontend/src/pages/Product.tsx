import Carousel from "../components/product/Carousel";
import Review from "../components/product/Review";

export default function Product() {
  return (
    <div className="product-main mt-5 flex-column row-gap-5">
      <div className="product-main-top d-flex column-gap-5 row-gap-5 w-100 h-100">
        <div className="product-main-top-left w-md-50 w-75">
          <Carousel imgUrls={["/g-1.jpg", "/g-2.jpg", "/g-3.jpg"]} />
        </div>
        <div className="product-main-top-right d-flex flex-column row-gap-3 w-md-50 w-75">
          <div className="product-main-top-right-name h1">Product name</div>
          <div className="product-main-top-right-description">
            Lorem ipsum dolor sit, amet consectetur adipisicing elit. Enim
            dolor, doloribus fuga temporibus aspernatur odio rerum error labore
            adipisci magni pariatur ex facilis excepturi dicta consectetur r sit
            amet consectetur adipisicing elit. Sequi ipsa quis nam quas, veniam
            sapiente, nesciunt asperiores maxime odit, vitae adipisci quisquam
            cupiditate tenetur officiis error inventore? Dolorem, sit illum?
          </div>
          <div className="product-main-top-right-price">Price</div>
          <div className="product-main-top-right-brand">Brand</div>
          <div className="product-main-top-right-stock">Stock status</div>

          <div className="product-main-top-right-shipping">Shipping status</div>
          <div className="product-main-top-right-buttons d-flex flex-column align-items-end row-gap-3">
            <label
              className="d-flex flex-row column-gap-3 align-items-center"
              htmlFor="Menge"
            >
              <span className="product-amount-title">Menge:</span>

              <input
                className="border rounded p-2 fs-4"
                value={1}
                type="number"
                id="rating"
                name="rating"
                min="1"
                max="5"
                step="1"
                required
              />
            </label>
            <button className="bg-success text-white px-4 py-2 d-flex flex-row column-gap-3">
              <img width="25px" src="/cart-wh.svg" alt="" />
              In den Warenkorb
            </button>
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
