import Carousel from "../components/product/Carousel";
import Review from "../components/product/Review";

import { mockProduct } from "../types/temp/PlaceholderData";

export default function Product() {
  return (
    <div className="product-main mt-5 flex-column row-gap-5">
      <div className="product-main-top d-flex column-gap-5 row-gap-5 w-100 h-100">
        <div className="product-main-top-left w-md-50 w-75">
          <Carousel imgUrls={["/g-1.jpg", "/g-2.jpg", "/g-3.jpg"]} />
        </div>
        <div className="product-main-top-right d-flex flex-column row-gap-3 w-md-50 w-75">
          <div className="product-main-top-right-name h1">
            {mockProduct.name}
          </div>
          <div className="product-main-top-right-description">
            {mockProduct.description}
          </div>
          <div className="product-main-top-right-price">
            ${mockProduct.price.toFixed(2)}
          </div>
          <div className="product-main-top-right-brand">
            {mockProduct.brand}
          </div>
          <div className="product-main-top-right-stock">
            {mockProduct.stockStatus ? "In Stock" : "Out of Stock"}
          </div>
          <div className="product-main-top-right-shipping">
            Shipping: {mockProduct.shippingTime} days, $
            {mockProduct.shippingCost.toFixed(2)}
          </div>
          <div className="product-main-top-right-buttons d-flex flex-column align-items-end row-gap-3">
            <label
              className="d-flex flex-row column-gap-3 align-items-center"
              htmlFor="quantity"
            >
              <span className="product-amount-title">Menge:</span>
              <input
                className="border rounded p-2 fs-4"
                value={1}
                type="number"
                id="quantity"
                name="quantity"
                min={1}
                max={5}
                step={1}
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
