import { useState } from "react";
import ProductsList from "../../components/admin/ProductsList";
import OrdersList from "../../components/admin/OrdersList";
// import UsersList from "../../components/admin/UsersList";
// import MessagesList from "../../components/admin/MessagesList";
// import OrdersList from "../../components/admin/OrdersList";

export default function AdminPage() {
  const [selected, setSelected] = useState("bestellungen");

  return (
    <div className="d-flex flex-column container py-4">
      <h1 className="mb-4">Admin-Übersicht</h1>

      <div className="d-flex gap-2 mb-4 pb-3 border-bottom">
        <button
          className={
            selected === "bestellungen"
              ? "admin-nav-button--selected d-flex align-items-center gap-2"
              : "admin-nav-button d-flex align-items-center gap-2"
          }
          onClick={() => setSelected("bestellungen")}
        >
          <img src="/orders.svg" alt="Bestellungen" />
          Bestellungen
        </button>

        <button
          className={
            selected === "produkte"
              ? "admin-nav-button--selected d-flex align-items-center gap-2"
              : "admin-nav-button d-flex align-items-center gap-2"
          }
          onClick={() => setSelected("produkte")}
        >
          <img src="/products.svg" alt="Produkte" />
          Produkte
        </button>

        <button
          className={
            selected === "benutzer"
              ? "admin-nav-button--selected d-flex align-items-center gap-2"
              : "admin-nav-button d-flex align-items-center gap-2"
          }
          onClick={() => setSelected("benutzer")}
        >
          <img src="/users.svg" alt="Benutzer" />
          Benutzer
        </button>

        <button
          className={
            selected === "nachrichten"
              ? "admin-nav-button--selected d-flex align-items-center gap-2"
              : "admin-nav-button d-flex align-items-center gap-2"
          }
          onClick={() => setSelected("nachrichten")}
        >
          <img src="/chat.svg" alt="Nachrichten" />
          Nachrichten
        </button>
      </div>

      <div>
        {selected === "bestellungen" && <OrdersList />}
        {selected === "produkte" && <ProductsList />}
        {/* {selected === "benutzer" && <UsersList />}
        {selected === "nachrichten" && <MessagesList />} */}
      </div>
    </div>
  );
}
