import { useState } from "react";
import { Order } from "../../components/order/Order";
import CartItem from "../../components/cart/CartItem";

export default function AdminPage() {
  const [selected, setSelected] = useState("bestellungen");

  return (
    <div className="d-flex flex-column container py-4">
      <h1 className="mb-4">Admin Dashboard</h1>

      <div className="d-flex gap-2 mb-4">
        {["bestellungen", "produkte", "benutzer"].map((section) => (
          <button
            key={section}
            className={`btn ${
              selected === section ? "btn-primary" : "btn-outline-primary"
            }`}
            onClick={() => setSelected(section)}
          >
            {section.charAt(0).toUpperCase() + section.slice(1)}
          </button>
        ))}
      </div>

      <div>
        {selected === "bestellungen" && (
          <div className="table-responsive">
            <table className="table table-bordered">
              <thead>
                <tr>
                  <th>Bestellnummer</th>
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
        )}

        {selected === "produkte" && (
          <div className="d-flex flex-column gap-3">
            {Array.from({ length: 3 }).map((_, index) => (
              <CartItem editable={true} key={index} />
            ))}
          </div>
        )}

        {selected === "benutzer" && (
          <div className="d-flex flex-column gap-3">
            <div className="border rounded p-3 d-flex justify-content-between align-items-center">
              <div>
                <div>
                  <strong>Name</strong>
                </div>
                <div>email@mail.ma</div>
                <div>Rolle: Admin</div>
              </div>
            </div>
            <div className="border rounded p-3 d-flex justify-content-between align-items-center">
              <div>
                <div>
                  <strong>Name</strong>
                </div>
                <div>email@mail.ma</div>
                <div>Rolle: Admin</div>
              </div>
            </div>
            <div className="border rounded p-3 d-flex justify-content-between align-items-center">
              <div>
                <div>
                  <strong>Name</strong>
                </div>
                <div>email@mail.ma</div>
                <div>Rolle: Admin</div>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}
