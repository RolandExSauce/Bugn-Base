import { useState } from "react";
import OrdersList from "../../components/admin/OrdersList";
import ProductsList from "../../components/admin/ProductsList";
import UsersList from "../../components/admin/UsersList";

export default function AdminPage() {
  const [selected, setSelected] = useState("orders");

  return (
    <div className="d-flex flex-column container py-4">
      <h1 className="mb-4">Admin Dashboard</h1>

      <div className="d-flex gap-2 mb-4 pb-3 border-bottom">
        {["orders", "products", "users"].map((section) => (
          <button
            key={section}
            className={
              selected === section
                ? "admin-nav-button--selected"
                : "admin-nav-button"
            }
            onClick={() => setSelected(section)}
          >
            {section.charAt(0).toUpperCase() + section.slice(1)}
          </button>
        ))}
      </div>

      <div>
        {selected === "orders" && <OrdersList />}

        {selected === "products" && <ProductsList />}

        {selected === "users" && <UsersList />}
      </div>
    </div>
  );
}
