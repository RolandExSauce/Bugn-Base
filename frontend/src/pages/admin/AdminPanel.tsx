import { useState } from "react";
import OrdersList from "../../components/admin/OrdersList";
import ProductsList from "../../components/admin/ProductsList";
import UsersList from "../../components/admin/UsersList";
import MessagesList from "../../components/admin/MessagesList";

const tabs = [
  { key: "bestellungen", label: "Bestellungen", icon: "/orders.svg" },
  { key: "produkte", label: "Produkte", icon: "/products.svg" },
  { key: "benutzer", label: "Benutzer", icon: "/users.svg" },
  { key: "nachrichten", label: "Nachrichten", icon: "/chat.svg" },
];

export default function AdminPage() {
  const [selected, setSelected] = useState("bestellungen");

  return (
    <div className="d-flex flex-column container py-4">
      <h1 className="mb-4">Admin-Übersicht</h1>

      <div className="d-flex flex-wrap gap-2 mb-4 pb-3 border-bottom">
        {tabs.map(({ key, label, icon }) => (
          <button
            key={key}
            className={`d-flex align-items-center gap-2 ${
              selected === key ? "admin-nav-button--selected" : "admin-nav-button"
            }`}
            onClick={() => setSelected(key)}
          >
            <img src={icon} alt={label} />
            <span className="d-none d-sm-inline">{label}</span>
          </button>
        ))}
      </div>

      <div>
        {selected === "bestellungen" && <OrdersList />}
        {selected === "produkte" && <ProductsList />}
        {selected === "benutzer" && <UsersList />}
        {selected === "nachrichten" && <MessagesList />}
      </div>
    </div>
  );
}