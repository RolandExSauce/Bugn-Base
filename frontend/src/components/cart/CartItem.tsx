const CartItem = ({ editable }: { editable: boolean }) => {
  return (
    <div className="d-flex align-items-center justify-content-between border rounded p-3 mb-3 flex-md-row flex-column">
      <div className="d-flex align-items-center column-gap-3">
        <img
          src="/g-1.jpg"
          alt="Product 1"
          width="50"
          height="50"
          className="rounded"
        />
        <span
          className="cart-item-name fs-5 text-truncate"
          style={{ width: "250px" }}
        >
          Product 1asdfsadfsadfsdafsadfsdaf asdfsadfsdaf
        </span>
      </div>
      <div className="d-flex flex-column align-items-center">
        <div className="text-secondary">Menge</div>
        <div>2</div>
      </div>
      <div className="d-flex flex-column align-items-center">
        <div className="text-secondary">Einzelpreis</div>
        <div>500 €</div>
      </div>
      <div className="d-flex flex-column align-items-center">
        <div className="text-secondary">Gesamt</div>
        <div>1000 €</div>
      </div>
      {editable && (
        <button className="cart-remove-item-button bg-danger text-white px-4 py-2">
          Entfernen
        </button>
      )}
    </div>
  );
};
export default CartItem;
