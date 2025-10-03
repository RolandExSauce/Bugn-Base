//use a card like element to display a single shop item
const ShopItem = () => {
  return (
    <div className="shop-item d-flex flex-column p-4 row-gap-1 bg-white">
      <div className="shop-item-image mw-100">
        <img src="/g-1.jpg" alt="" />
      </div>
      <div className="shop-item-name h4">Name of product</div>
      <div className="shop-item-price">Price</div>
      <div className="shop-item-brand">Brand</div>
      <div className="shop-item-stock">Stock</div>
    </div>
  );
};
export default ShopItem;
