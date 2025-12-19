import type { Product } from "../../types/models";

interface ShopItemProps {
  product: Product;
}

const ShopItem = ({ product }: ShopItemProps) => {
  const getStockStatus = () => {
    switch (product.stockStatus) {
      case "IN_STOCK":
        return { text: "Auf Lager", className: "text-success" };
      case "OUT_OF_STOCK":
        return { text: "Nicht verfügbar", className: "text-danger" };
      default:
        return { text: "Status unbekannt", className: "text-secondary" };
    }
  };

  const stockInfo = getStockStatus();

  const productImage =
    product.images && product.images.length > 0
      ? `${import.meta.env.VITE_BASE_URL}/media${product.images[0].url}`
      : "/no_found_placeholder.jpg";

  return (
    <div className="shop-item d-flex align-items-center flex-column p-4 row-gap-1 bg-white rounded shadow-sm">
      <div className="shop-item-image mb-3">
        <img src={productImage} alt={product.name} className="p-1" />
      </div>

      <div className="shop-item-name h5 text-center fw-bold mb-2">
        {product.name}
      </div>

      <div className="shop-item-price h4 text-primary mb-2">
        €{product.price.toFixed(2)}
      </div>

      <div className="shop-item-brand text-muted mb-2">{product.brand}</div>

      <div className={`shop-item-stock ${stockInfo.className} fw-medium`}>
        {stockInfo.text}
      </div>

      {product.shippingTime > 0 && (
        <div className="shop-item-shipping small text-muted mt-1">
          Lieferzeit: {product.shippingTime} Tag
          {product.shippingTime !== 1 ? "e" : ""}
        </div>
      )}

      <div className="shop-item-category mt-2">
        <span className="badge bg-light text-dark border">
          {product.category}
        </span>
      </div>
    </div>
  );
};
export default ShopItem;
