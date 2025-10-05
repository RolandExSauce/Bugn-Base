import { Order } from "../order/Order";

export default function OrderList() {
  return (
    <div className="table-responsive">
      <table className="table table-bordered">
        <thead>
          <tr>
            <th>Bestellung</th>
            <th>Kunde</th>
            <th>Datum</th>
            <th>Status</th>
            <th>Aktion</th>
          </tr>
        </thead>
        <tbody>
          <td>
            <Order />
          </td>
          <td>Kundenname</td>
          <td>Status</td>
          <td>Kundenname</td>
          <td className="d-flex gap-2">
            <select className="form-select select-no-arrow ">
              <option value="pending">pending</option>
              <option value="shipped">shipped</option>
              <option value="delivered">delivered</option>
            </select>
            <button className=" btn-danger">Stornieren</button>
          </td>
        </tbody>
        <tbody>
          <td>
            <Order />
          </td>
          <td>Kundenname</td>
          <td>Status</td>
          <td>Kundenname</td>
          <td className="d-flex gap-2">
            <button className=" btn-success">Nächste</button>
            <button className=" btn-danger">Stornieren</button>
          </td>
        </tbody>
        <tbody>
          <td>
            <Order />
          </td>
          <td>Kundenname</td>
          <td>Status</td>
          <td>Kundenname</td>
          <td className="d-flex gap-2">
            <button className=" btn-success">Nächste</button>
            <button className=" btn-danger">Stornieren</button>
          </td>
        </tbody>
      </table>
    </div>
  );
}
