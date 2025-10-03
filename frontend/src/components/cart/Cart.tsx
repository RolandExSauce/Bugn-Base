const Cart = () => {
  return (
    <div className="cart-main">
      <span className="cart-title">Warenkorb</span>
      <div className="cart-listing">
        <table>
          <thead>
            <tr>
              <th>Produkt</th>
              <th>Menge</th>
              <th>Einzelpreis</th>
              <th>Gesamt</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>
                <img width="50px" height="50px" src="/g-1.jpg" alt="" />
              </td>
              <td>2</td>
              <td>500 €</td>
              <td>1000 €</td>
            </tr>
            <tr>
              <td>Klavier</td>
              <td>1</td>
              <td>3000 €</td>
              <td>3000 €</td>
            </tr>
          </tbody>
          <tfoot>
            <tr>
              <td>
                <strong>Summe</strong>
              </td>
              <td>
                <strong>4000 €</strong>
              </td>
            </tr>
          </tfoot>
        </table>
      </div>
    </div>
  );
};
export default Cart;
