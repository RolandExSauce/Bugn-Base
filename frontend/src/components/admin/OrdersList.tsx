import type { Order as OrderType } from "../../types/models";
import FullOrder from "../../components/admin/FullOrder";
import { mockOrder } from "../../api/mock";

export default function OrderList() {
  const orders: OrderType[] = Array.from({ length: 3 }, () => mockOrder);

  // todo: implement fetching orders and storing them in state

  return (
    <div className="table-responsive">
      <table className="table table-bordered">
        <thead>
          <tr>
            <th>Bestellnummer</th>
            <th>Kunde</th>
            <th>Datum</th>
            <th>Gesamtbetrag</th>
            <th>Lieferadresse</th>
            <th>Status</th>
            <th>Zahlungsmethode</th>
            <th>Bestellte Artikel</th>
            <th>Aktionen</th>
          </tr>
        </thead>
        <tbody>
          {orders.map((order, i) => {
            return <FullOrder key={i} order={order} />;
          })}
        </tbody>
      </table>
    </div>
  );
}
