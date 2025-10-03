//use a card like element to display a single shop item
const ProductItem = () => {
  return (
    <div className="product-item">
      <div className="product-item-image">
        <img src="/g-1.jpg" alt="" />
      </div>
      <div className="product-item-name">Name of product</div>
      <div className="product-item-price">Price</div>
      <div className="product-item-brand">Brand</div>
      <div className="priduct-item-stock">Stock</div>
    </div>
  );
};
export default ProductItem;
