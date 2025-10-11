import { useState } from "react";
import type { Product } from "../../types/models";
import AdminService from "../../services/admin/admin.service";

interface FullProductProps {
  initialProduct: Product;
}

export default function FullProduct({ initialProduct }: FullProductProps) {
  const [product, setProduct] = useState<Product>(initialProduct);

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const handleChange = (key: keyof Product, value: any) => {
    setProduct((prev) => ({
      ...prev,
      [key]: value,
    }));
  };

  const handleSave = () => {
    AdminService.updateProduct(product);
  };

  const handleDelete = () => {
    AdminService.deleteProduct(product.id);
  };

  return (
    <tr>
      <td>
        <input
          type="text"
          value={product.name}
          onChange={(e) => handleChange("name", e.target.value)}
          className="form-control"
        />
      </td>

      <td>
        <select
          value={product.category}
          onChange={(e) =>
            handleChange(
              "category",
              e.target.value as "piano" | "guitar" | "violin"
            )
          }
          className="form-control"
        >
          <option value="piano">Piano</option>
          <option value="guitar">Guitar</option>
          <option value="violin">Violin</option>
        </select>
      </td>

      <td>
        <textarea
          value={product.description}
          onChange={(e) => handleChange("description", e.target.value)}
          className="form-control"
          rows={2}
        />
      </td>

      <td>
        <input
          type="number"
          value={product.price}
          onChange={(e) => handleChange("price", parseFloat(e.target.value))}
          className="form-control"
        />
      </td>

      <td>
        <input
          type="number"
          value={product.shippingCost}
          onChange={(e) =>
            handleChange("shippingCost", parseFloat(e.target.value))
          }
          className="form-control"
        />
      </td>

      <td>
        <input
          type="text"
          value={product.brand}
          onChange={(e) => handleChange("brand", e.target.value)}
          className="form-control"
        />
      </td>

      <td>
        <select
          value={product.stockStatus ? "in" : "out"}
          onChange={(e) => handleChange("stockStatus", e.target.value === "in")}
          className="form-control"
        >
          <option value="in">In Stock</option>
          <option value="out">Out of Stock</option>
        </select>
      </td>

      <td>
        <input
          type="number"
          value={product.shippingTime}
          onChange={(e) =>
            handleChange("shippingTime", parseInt(e.target.value))
          }
          className="form-control"
        />
      </td>

      <td className="text-center">
        <input
          type="checkbox"
          checked={product.active}
          onChange={(e) => handleChange("active", e.target.checked)}
        />
      </td>

      <td className="d-flex column-gap-2">
        <button className="admin-user-action-button" onClick={handleSave}>
          <img
            width="25px"
            height="25px"
            src="/update.svg"
            alt="Update user button icon"
          />
        </button>
        <button className="admin-user-action-button" onClick={handleDelete}>
          <img
            width="25px"
            height="25px"
            src="/delete.svg"
            alt="Delete user button icon"
          />
        </button>
      </td>
    </tr>
  );
}
