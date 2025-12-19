import { useEffect, useRef, useState } from "react";
import { useParams } from "react-router-dom";
import Carousel from "../components/product/Carousel";
import Review from "../components/product/Review";
import { useCartContext } from "../context/CartContext";
import type { Product } from "../types/models";
import ShopService from "../services/shop.service"; // adjust import path

export default function Product() {
  const { productId } = useParams();
  const [product, setProduct] = useState<Product | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [quantity, setQuantity] = useState(1);
  const { addItem } = useCartContext();
  const buttonRef = useRef<HTMLButtonElement>(null);

  useEffect(() => {
    const fetchProduct = async () => {
      if (!productId) return;
      setLoading(true);
      setError(null);

      try {
        const data = await ShopService.getProduct(productId);
        setProduct(data);
      } catch (err) {
        console.error("Error fetching product:", err);
        setError("Produkt konnte nicht geladen werden.");
      } finally {
        setLoading(false);
      }
    };

    fetchProduct();
  }, [productId]);

  const handleAddToCart = () => {
    if (!product) return;

    addItem({ product, quantity });

    buttonRef.current?.classList.remove("green-button-success");
    void buttonRef.current?.offsetWidth;
    buttonRef.current?.classList.add("green-button-success");
  };

  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center py-5">
        <div className="spinner-border text-primary" role="status" />
        <span className="ms-3">Lade Produkt...</span>
      </div>
    );
  }

  if (error) {
    return <div className="alert alert-danger mt-5">{error}</div>;
  }

  if (!product) return null;

  return (
    <div className="product-main mt-5 flex-column row-gap-5">
      <div className="product-main-top d-flex column-gap-5 row-gap-5 w-100 h-100">
        <div className="product-main-top-left w-md-50 w-75">
          <Carousel imgUrls={product.images?.map((img) => img.url) || []} />
        </div>
        <div className="product-main-top-right d-flex flex-column row-gap-3 w-md-50 w-75">
          <div className="product-main-top-right-name h1">{product.name}</div>
          <div className="product-main-top-right-description">
            {product.description}
          </div>
          <div className="product-main-top-right-price">
            ${product.price.toFixed(2)}
          </div>
          <div className="product-main-top-right-brand">{product.brand}</div>
          <div className="product-main-top-right-stock">
            {product.stockStatus === "IN_STOCK" ? "In Stock" : "Out of Stock"}
          </div>
          <div className="product-main-top-right-shipping">
            Shipping: {product.shippingTime} days, $
            {product.shippingCost.toFixed(2)}
          </div>
          <div className="product-main-top-right-buttons d-flex flex-column align-items-end row-gap-3">
            <label
              className="d-flex flex-row column-gap-3 align-items-center"
              htmlFor="quantity"
            >
              <span className="product-amount-title">Menge:</span>
              <input
                className="border rounded p-2 fs-4"
                value={quantity}
                onChange={(e) => setQuantity(e.target.valueAsNumber)}
                type="number"
                id="quantity"
                name="quantity"
                min={1}
                max={5}
                step={1}
                required
              />
            </label>
            <button
              ref={buttonRef}
              onClick={handleAddToCart}
              className="add-cart-button text-white px-4 py-2 d-flex flex-row column-gap-3"
            >
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
