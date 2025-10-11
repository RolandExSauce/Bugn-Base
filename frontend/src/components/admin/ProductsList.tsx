import { useState } from "react";
import FullProduct from "../../components/admin/FullProduct";
import { mockProduct } from "../../types/temp/PlaceholderData";
import type { Product } from "../../types/models";

export default function ProductsList() {
  const [products, setProducts] = useState(
    Array.from({ length: 10 }, (_, i) => ({
      ...mockProduct,
      id: String(i + 1),
    }))
  );
  const [showNewForm, setShowNewForm] = useState(false);

  const handleAddProduct = (newProduct: Product) => {
    setProducts((prev) => [
      ...prev,
      { ...newProduct, id: String(prev.length + 1) },
    ]);
    setShowNewForm(false);
  };

  return (
    <div className="d-flex flex-column">
      <button
        className="btn bg-success mb-3 text-white fw-bold fs-3"
        style={{ height: "60px" }}
        onClick={() => setShowNewForm((prev) => !prev)}
      >
        Add Product
      </button>

      {showNewForm && (
        <div className="mb-3 p-3 border rounded">
          <div className="d-flex flex-column row-gap-3">
            <label className="form-label">
              Name
              <input
                type="text"
                className="form-control"
                placeholder="Product name"
              />
            </label>

            <label className="form-label">
              Category
              <select className="form-select">
                <option value="piano">Piano</option>
                <option value="guitar">Guitar</option>
                <option value="violin">Violin</option>
              </select>
            </label>

            <label className="form-label">
              Description
              <textarea
                className="form-control"
                rows={3}
                placeholder="Product description"
              />
            </label>

            <label className="form-label">
              Price
              <input
                type="number"
                className="form-control"
                placeholder="0.00"
              />
            </label>

            <label className="form-label">
              Shipping Cost
              <input
                type="number"
                className="form-control"
                placeholder="0.00"
              />
            </label>

            <label className="form-label">
              Brand
              <input
                type="text"
                className="form-control"
                placeholder="Brand name"
              />
            </label>

            <label className="form-label">
              Stock Status
              <select className="form-select">
                <option value="in">In Stock</option>
                <option value="out">Out of Stock</option>
              </select>
            </label>

            <label className="form-label">
              Shipping Time (days)
              <input type="number" className="form-control" placeholder="0" />
            </label>

            <label className="form-label d-flex align-items-center column-gap-2">
              Active
              <input type="checkbox" />
            </label>
          </div>

          <div className="d-flex column-gap-2 mt-3">
            <button className="btn btn-success">Save</button>
            <button className="btn btn-secondary">Cancel</button>
          </div>
        </div>
      )}

      <table className="table table-bordered table-striped">
        <thead>
          <tr>
            <th>Name</th>
            <th>Category</th>
            <th>Description</th>
            <th>Price</th>
            <th>Shipping Cost</th>
            <th>Brand</th>
            <th>Stock Status</th>
            <th>Shipping Time</th>
            <th>Active</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {products.map((product) => (
            <FullProduct key={product.id} initialProduct={product} />
          ))}
        </tbody>
      </table>
    </div>
  );
}
