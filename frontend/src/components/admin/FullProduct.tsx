import { useState, useEffect, useRef } from "react";
import type { Product } from "../../types/models";
import AdminService from "../../services/admin/admin.service";
import AdminUpdateButton from "../common/AdminUpdateButton";
import AdminDeleteButton from "../common/AdminDeleteButton";
import AdminSelectRowButton from "../common/AdminSelectRowButton";

import { NAME_REGEX, PRICE_REGEX, TEXT_REGEX } from "../../types/regex";

interface FullProductProps {
  initialProduct: Product;
  selectedProductId: string | null;
  handleSelect: (id: string) => void;
}

export default function FullProduct({
  initialProduct,
  selectedProductId,
  handleSelect,
}: FullProductProps) {
  const [product, setProduct] = useState<Product>(initialProduct);
  const [isEdited, setIsEdited] = useState(false);
  const trRef = useRef<HTMLTableRowElement>(null);

  const [invalidInput, setInvalidInput] = useState({
    name: false,
    description: false,
    price: false,
    shippingCost: false,
    brand: false,
    shippingTime: false,
  });

  useEffect(() => {
    setProduct(initialProduct);
  }, [initialProduct]);

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const handleChange = (key: keyof Product, value: any) => {
    setIsEdited(true);
    setProduct((prev) => ({
      ...prev,
      [key]: value,
    }));
  };

  const handleSave = () => {
    const newInvalidInput = {
      name: false,
      description: false,
      price: false,
      shippingCost: false,
      brand: false,
      shippingTime: false,
    };

    let invalid = false;

    if (!NAME_REGEX.test(product.name)) {
      newInvalidInput.name = true;
      invalid = true;
    }

    if (!TEXT_REGEX.test(product.description)) {
      newInvalidInput.description = true;
      invalid = true;
    }

    if (!PRICE_REGEX.test(String(product.price))) {
      newInvalidInput.price = true;
      invalid = true;
    }

    if (!PRICE_REGEX.test(String(product.shippingCost))) {
      newInvalidInput.shippingCost = true;
      invalid = true;
    }

    if (!NAME_REGEX.test(product.brand)) {
      newInvalidInput.brand = true;
      invalid = true;
    }

    if (!PRICE_REGEX.test(String(product.shippingTime))) {
      newInvalidInput.shippingTime = true;
      invalid = true;
    }

    if (invalid) {
      setInvalidInput(newInvalidInput);
      return;
    }

    setInvalidInput(newInvalidInput);
    // AdminService.updateProduct(product);

    trRef.current?.classList.remove("user-row-success");
    void trRef.current?.offsetWidth;
    trRef.current?.classList.add("user-row-success");
    handleSelect("");
  };

  const handleDelete = () => {
    AdminService.deleteProduct(product.id);
  };

  if (selectedProductId === product.id) {
    return (
      <tr ref={trRef} className="editable-user-row">
        <td>
          <input
            type="text"
            value={product.name}
            onChange={(e) => handleChange("name", e.target.value)}
            className="form-control"
          />
          {invalidInput.name && (
            <span className="text-danger d-block invalid-input">
              Invalid name
            </span>
          )}
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
          {invalidInput.description && (
            <span className="text-danger d-block invalid-input">
              Invalid description
            </span>
          )}
        </td>

        <td>
          <input
            type="number"
            value={product.price}
            onChange={(e) => handleChange("price", parseFloat(e.target.value))}
            className="form-control"
          />
          {invalidInput.price && (
            <span className="text-danger d-block invalid-input">
              Invalid price
            </span>
          )}
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
          {invalidInput.shippingCost && (
            <span className="text-danger d-block invalid-input">
              Invalid shipping cost
            </span>
          )}
        </td>

        <td>
          <input
            type="text"
            value={product.brand}
            onChange={(e) => handleChange("brand", e.target.value)}
            className="form-control"
          />
          {invalidInput.brand && (
            <span className="text-danger d-block invalid-input">
              Invalid brand
            </span>
          )}
        </td>

        <td>
          <select
            value={product.stockStatus ? "in" : "out"}
            onChange={(e) =>
              handleChange("stockStatus", e.target.value === "in")
            }
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
          {invalidInput.shippingTime && (
            <span className="text-danger d-block invalid-input">
              Invalid shipping time
            </span>
          )}
        </td>

        <td className="text-center">
          <input
            type="checkbox"
            checked={product.active}
            onChange={(e) => handleChange("active", e.target.checked)}
          />
        </td>

        <td className="d-flex column-gap-2">
          <AdminUpdateButton disabled={isEdited} action={handleSave} />

          <button
            className="admin-user-action-button"
            onClick={() => {
              handleSelect("");
            }}
          >
            <img width="25px" height="25px" src="/undo.svg" alt="undo" />
          </button>
        </td>
      </tr>
    );
  }

  return (
    <tr key={product.id}>
      <td>{product.name}</td>
      <td>{product.category}</td>
      <td>{product.description}</td>
      <td>{product.price}</td>
      <td>{product.shippingCost}</td>
      <td>{product.brand}</td>
      <td>{product.stockStatus ? "In Stock" : "Out of Stock"}</td>
      <td>{product.shippingTime}</td>
      <td className="text-center">
        <input type="checkbox" checked={product.active} disabled />
      </td>

      <td className="d-flex column-gap-2">
        <AdminSelectRowButton action={() => handleSelect(product.id)} />
        <AdminDeleteButton action={handleDelete} />
      </td>
    </tr>
  );
}
