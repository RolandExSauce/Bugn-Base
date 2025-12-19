import { useState, useEffect, useRef } from "react";
import type { Product, ProductDTO } from "../../types/models";
import { AdminProductService } from "../../services";

interface FullProductProps {
  product: Product;
  isSelected: boolean;
  onSelect: () => void;
  onUpdate: (updatedProduct: Product) => void;
  onDelete: (productId: number) => void;
}

export default function FullProduct({
  product: initialProduct,
  isSelected,
  onSelect,
  onUpdate,
  onDelete,
}: FullProductProps) {
  const [product, setProduct] = useState<Product>(initialProduct);
  const [isEdited, setIsEdited] = useState(false);
  const [isSaving, setIsSaving] = useState(false);
  const trRef = useRef<HTMLTableRowElement>(null);

  const [invalidInput, setInvalidInput] = useState({
    name: false,
    price: false,
    shippingCost: false,
    brand: false,
    shippingTime: false,
  });

  useEffect(() => {
    setProduct(initialProduct);
  }, [initialProduct]);

  const handleChange = (key: keyof Product, value: any) => {
    setIsEdited(true);
    setProduct((prev) => ({
      ...prev,
      [key]: value,
    }));
  };

  const validateForm = (): boolean => {
    const newInvalidInput = {
      name: false,
      price: false,
      shippingCost: false,
      brand: false,
      shippingTime: false,
    };

    let isValid = true;

    if (!product.name || product.name.trim().length === 0) {
      newInvalidInput.name = true;
      isValid = false;
    }

    if (!product.price || product.price <= 0) {
      newInvalidInput.price = true;
      isValid = false;
    }

    if (!product.shippingCost || product.shippingCost < 0) {
      newInvalidInput.shippingCost = true;
      isValid = false;
    }

    if (!product.brand || product.brand.trim().length === 0) {
      newInvalidInput.brand = true;
      isValid = false;
    }

    if (
      !product.shippingTime ||
      product.shippingTime < 1 ||
      product.shippingTime > 5
    ) {
      newInvalidInput.shippingTime = true;
      isValid = false;
    }

    setInvalidInput(newInvalidInput);
    return isValid;
  };

  const handleProductSave = async () => {
    if (!validateForm()) return;

    setIsSaving(true);
    try {
      const productDTO: ProductDTO = {
        name: product.name,
        category: product.category,
        description: product.description,
        price: product.price,
        shippingCost: product.shippingCost,
        brand: product.brand,
        stockStatus: product.stockStatus,
        shippingTime: product.shippingTime,
        active: product.active,
      };

      await AdminProductService.updateProduct(
        product.id.toString(),
        productDTO
      );

      // Success animation
      trRef.current?.classList.remove("user-row-success");
      void trRef.current?.offsetWidth;
      trRef.current?.classList.add("user-row-success");

      onUpdate(product);
      setIsEdited(false);
      setTimeout(() => onSelect(), 800);
    } catch (error) {
      console.error("Error updating product:", error);
    } finally {
      setIsSaving(false);
    }
  };

  const handleDelete = async () => {
    if (!window.confirm(`Möchten Sie "${product.name}" wirklich löschen?`))
      return;

    try {
      await AdminProductService.deleteProduct(product.id.toString());
      onDelete(product.id);
    } catch (error) {
      console.error("Error deleting product:", error);
    }
  };

  const handleCancel = () => {
    setProduct(initialProduct);
    setIsEdited(false);
    onSelect();
  };

  if (!isSelected) {
    return (
      <tr>
        <td>{product.id}</td>
        <td>{product.name}</td>
        <td>{product.category}</td>
        <td>{product.description}</td>
        <td>€{product.price.toFixed(2)}</td>
        <td>€{product.shippingCost.toFixed(2)}</td>
        <td>{product.brand}</td>
        <td>
          <span
            className={`badge ${
              product.stockStatus === "IN_STOCK"
                ? "bg-success"
                : product.stockStatus === "LOW_STOCK"
                ? "bg-warning"
                : "bg-danger"
            }`}
          >
            {product.stockStatus === "IN_STOCK"
              ? "Auf Lager"
              : product.stockStatus === "LOW_STOCK"
              ? "Gering"
              : "Nicht auf Lager"}
          </span>
        </td>
        <td>{product.shippingTime} Tage</td>
        <td className="text-center">
          <input
            type="checkbox"
            checked={product.active}
            readOnly
            className="form-check-input"
          />
        </td>
        <td>
          <div className="d-flex gap-2">
            <button
              className="btn btn-sm btn-outline-primary d-flex align-items-center gap-1"
              onClick={onSelect}
            >
              <img
                src="/update.svg"
                alt="Bearbeiten"
                style={{ width: 14, height: 14 }}
              />
            </button>
            <button
              className="btn btn-sm btn-outline-danger d-flex align-items-center gap-1"
              onClick={handleDelete}
            >
              <img
                src="/delete.svg"
                alt="Löschen"
                style={{ width: 14, height: 14 }}
              />
            </button>
          </div>
        </td>
      </tr>
    );
  }

  return (
    <tr ref={trRef} className="table-warning">
      <td>{product.id}</td>
      <td>
        <input
          type="text"
          value={product.name}
          onChange={(e) => handleChange("name", e.target.value)}
          className={`form-control form-control-sm ${
            invalidInput.name ? "is-invalid" : ""
          }`}
        />
        {invalidInput.name && (
          <div className="invalid-feedback">Name ist erforderlich</div>
        )}
      </td>
      <td>
        <select
          value={product.category}
          onChange={(e) => handleChange("category", e.target.value)}
          className="form-control form-control-sm"
        >
          <option value="GUITARS">Gitarre</option>
          <option value="PIANOS">Klavier</option>
          <option value="VIOLINS">Violine</option>
        </select>
      </td>
      <td>
        <textarea
          value={product.description}
          onChange={(e) => handleChange("description", e.target.value)}
          className="form-control form-control-sm"
          rows={2}
        />
      </td>
      <td>
        <input
          type="number"
          step="0.01"
          min="0.01"
          value={product.price}
          onChange={(e) => handleChange("price", parseFloat(e.target.value))}
          className={`form-control form-control-sm ${
            invalidInput.price ? "is-invalid" : ""
          }`}
        />
        {invalidInput.price && (
          <div className="invalid-feedback">Preis muss positiv sein</div>
        )}
      </td>
      <td>
        <input
          type="number"
          min="0"
          value={product.shippingCost}
          onChange={(e) =>
            handleChange("shippingCost", parseInt(e.target.value))
          }
          className={`form-control form-control-sm ${
            invalidInput.shippingCost ? "is-invalid" : ""
          }`}
        />
        {invalidInput.shippingCost && (
          <div className="invalid-feedback">Versandkosten müssen ≥ 0 sein</div>
        )}
      </td>
      <td>
        <input
          type="text"
          value={product.brand}
          onChange={(e) => handleChange("brand", e.target.value)}
          className={`form-control form-control-sm ${
            invalidInput.brand ? "is-invalid" : ""
          }`}
        />
        {invalidInput.brand && (
          <div className="invalid-feedback">Marke ist erforderlich</div>
        )}
      </td>
      <td>
        <select
          value={product.stockStatus}
          onChange={(e) => handleChange("stockStatus", e.target.value)}
          className="form-control form-control-sm"
        >
          <option value="IN_STOCK">Auf Lager</option>
          <option value="LOW_STOCK">Geringer Lagerbestand</option>
          <option value="OUT_OF_STOCK">Nicht auf Lager</option>
        </select>
      </td>
      <td>
        <input
          type="number"
          min="1"
          max="5"
          value={product.shippingTime}
          onChange={(e) =>
            handleChange("shippingTime", parseInt(e.target.value))
          }
          className={`form-control form-control-sm ${
            invalidInput.shippingTime ? "is-invalid" : ""
          }`}
        />
        {invalidInput.shippingTime && (
          <div className="invalid-feedback">1-5 Tage</div>
        )}
      </td>
      <td className="text-center">
        <input
          type="checkbox"
          checked={product.active}
          onChange={(e) => handleChange("active", e.target.checked)}
          className="form-check-input"
        />
      </td>
      <td>
        <div className="d-flex gap-2">
          <button
            className="btn btn-sm btn-success d-flex align-items-center gap-1"
            onClick={handleProductSave}
            disabled={!isEdited || isSaving}
            title="Speichern"
          >
            <img
              src="/save.svg"
              alt="Speichern"
              style={{ width: 16, height: 16 }}
            />
          </button>
          <button
            className="btn btn-sm btn-secondary d-flex align-items-center gap-1"
            onClick={handleCancel}
            title="Abbrechen"
          >
            <img
              src="/undo.svg"
              alt="Abbrechen"
              style={{ width: 16, height: 16 }}
            />
          </button>
        </div>
      </td>
    </tr>
  );
}
