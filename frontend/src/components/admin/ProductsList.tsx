import { useEffect, useRef, useState } from "react";
import FullProduct from "../../components/admin/FullProduct";
import type { Product, ProductDTO } from "../../types/models";
import { AdminProductService } from "../../services";

export default function ProductsList() {
  const [selectedProductId, setSelectedProductId] = useState<string>("");
  const divRef = useRef<HTMLDivElement>(null);
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [showNewForm, setShowNewForm] = useState(false);
  const [uploadedImages, setUploadedImages] = useState<File[]>([]);

  console.log("products for admin: ", products);

  //or could use Partial Product type
  const [newProductForm, setNewProductForm] = useState<ProductDTO>({
    name: "",
    category: "GUITARS",
    description: "",
    price: 0,
    shippingCost: 0,
    brand: "",
    stockStatus: "IN_STOCK",
    shippingTime: 0,
    active: true,
  });

  const [newInvalid, setNewInvalid] = useState({
    name: false,
    description: false,
    price: false,
    shippingCost: false,
    brand: false,
    shippingTime: false,
  });

  const fetchProducts = async () => {
    try {
      setLoading(true);
      const data = await AdminProductService.getProducts();
      setProducts(data);
    } catch (error) {
      console.error("Error fetching products:", error);
    } finally {
      setLoading(false);
    }
  };

  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (!e.target.files) return;
    const filesArray = Array.from(e.target.files);
    setUploadedImages((prev) => [...prev, ...filesArray]);
  };

  const removeImage = (index: number) => {
    setUploadedImages((prev) => prev.filter((_, i) => i !== index));
  };

  useEffect(() => {
    fetchProducts();
  }, []);

  const handleSelectedProduct = (id: string) => {
    if (selectedProductId === id) {
      setSelectedProductId("");
    } else {
      setSelectedProductId(id);
    }
  };

  const handleNewChange = (key: keyof ProductDTO, value: any) => {
    setNewProductForm((prev) => ({ ...prev, [key]: value }));
  };

  const saveNewProduct = async () => {
    const nextInvalid = {
      name: false,
      description: false,
      price: false,
      shippingCost: false,
      brand: false,
      shippingTime: false,
    };

    let hasError = false;

    if (!newProductForm.name || newProductForm.name.trim().length === 0) {
      nextInvalid.name = true;
      hasError = true;
    }

    if (!newProductForm.price || newProductForm.price <= 0) {
      nextInvalid.price = true;
      hasError = true;
    }

    if (!newProductForm.shippingCost || newProductForm.shippingCost < 0) {
      nextInvalid.shippingCost = true;
      hasError = true;
    }

    if (!newProductForm.brand || newProductForm.brand.trim().length === 0) {
      nextInvalid.brand = true;
      hasError = true;
    }

    if (
      !newProductForm.shippingTime ||
      newProductForm.shippingTime < 1 ||
      newProductForm.shippingTime > 5
    ) {
      nextInvalid.shippingTime = true;
      hasError = true;
    }

    setNewInvalid(nextInvalid);
    if (hasError) return;

    try {
      // Create ProductDTO from form
      const productDTO: ProductDTO = {
        name: newProductForm.name,
        category: newProductForm.category,
        description: newProductForm.description,
        price: newProductForm.price,
        shippingCost: newProductForm.shippingCost,
        brand: newProductForm.brand,
        stockStatus: newProductForm.stockStatus,
        shippingTime: newProductForm.shippingTime,
        active: newProductForm.active ?? true,
      };

      // Save to backend
      const savedProduct = await AdminProductService.addProduct(productDTO);

      // Update local state
      setProducts((prev) => [...prev, savedProduct]);

      // Success animation
      divRef.current?.classList.remove("success-animation");
      void divRef.current?.offsetWidth;
      divRef.current?.classList.add("success-animation");

      // Reset form after delay
      setTimeout(() => {
        setShowNewForm(false);
        setNewProductForm({
          name: "",
          category: "GUITARS",
          description: "",
          price: 0,
          shippingCost: 0,
          brand: "",
          stockStatus: "IN_STOCK",
          shippingTime: 0,
          active: true,
        });
        setNewInvalid({
          name: false,
          description: false,
          price: false,
          shippingCost: false,
          brand: false,
          shippingTime: false,
        });
      }, 1000);
    } catch (error) {
      console.error("Error adding product:", error);
      // TODO: Show error message to user
    }
  };

  const handleProductUpdated = (updatedProduct: Product) => {
    setProducts((prev) =>
      prev.map((p) => (p.id === updatedProduct.id ? updatedProduct : p))
    );
  };

  const handleProductDeleted = (productId: number) => {
    setProducts((prev) => prev.filter((p) => p.id !== productId));
  };

  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center py-5">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
        <p className="mt-3 ms-3">Lade Produkte...</p>
      </div>
    );
  }

  return (
    <div className="d-flex flex-column">
      <button
        className="btn bg-success mb-3 text-white fw-bold fs-3"
        style={{ height: "60px" }}
        onClick={() => setShowNewForm((prev) => !prev)}
      >
        Produkt hinzufügen
      </button>

      {showNewForm && (
        <div
          ref={divRef}
          className="mb-3 p-3 border rounded add-new-product-div"
        >
          <div className="d-flex flex-column row-gap-3">
            <label className="form-label">
              Name
              <input
                type="text"
                className="form-control"
                placeholder="Produktname"
                value={newProductForm.name}
                onChange={(e) => handleNewChange("name", e.target.value)}
              />
              {newInvalid.name && (
                <div className="text-danger">Name ist erforderlich</div>
              )}
            </label>

            <label className="form-label">
              Kategorie
              <select
                className="form-select"
                value={newProductForm.category}
                onChange={(e) => handleNewChange("category", e.target.value)}
              >
                <option value="GUITARS">Gitarre</option>
                <option value="PIANOS">Klavier</option>
                <option value="VIOLINS">Violine</option>
              </select>
            </label>

            <label className="form-label">
              Beschreibung
              <textarea
                className="form-control"
                rows={3}
                placeholder="Produktbeschreibung"
                value={newProductForm.description}
                onChange={(e) => handleNewChange("description", e.target.value)}
              />
              {newInvalid.description && (
                <div className="text-danger">Beschreibung ist ungültig</div>
              )}
            </label>

            <label className="form-label">
              Preis (€)
              <input
                type="number"
                step="0.01"
                min="0.01"
                className={`form-control ${
                  newInvalid.price ? "is-invalid" : ""
                }`}
                placeholder="0.00"
                value={newProductForm.price}
                onChange={(e) =>
                  handleNewChange("price", parseFloat(e.target.value))
                }
              />
              {newInvalid.price && (
                <div className="text-danger">Preis ist ungültig</div>
              )}
            </label>

            <label className="form-label">
              Versandkosten (€)
              <input
                type="number"
                step="0.01"
                min="0"
                className={`form-control ${
                  newInvalid.shippingCost ? "is-invalid" : ""
                }`}
                placeholder="0.00"
                value={newProductForm.shippingCost}
                onChange={(e) =>
                  handleNewChange("shippingCost", parseInt(e.target.value))
                }
              />
              {newInvalid.shippingCost && (
                <div className="invalid-feedback">
                  Versandkosten müssen ≥ 0 sein
                </div>
              )}
            </label>

            <label className="form-label">
              Marke
              <input
                type="text"
                className={`form-control ${
                  newInvalid.brand ? "is-invalid" : ""
                }`}
                placeholder="Markenname"
                value={newProductForm.brand}
                onChange={(e) => handleNewChange("brand", e.target.value)}
              />
              {newInvalid.brand && (
                <div className="invalid-feedback">Marke ist erforderlich</div>
              )}
            </label>

            <label className="form-label">
              Lagerstatus
              <select
                className="form-select"
                value={newProductForm.stockStatus}
                onChange={(e) => handleNewChange("stockStatus", e.target.value)}
              >
                <option value="IN_STOCK">Auf Lager</option>
                <option value="LOW_STOCK">Geringer Lagerbestand</option>
                <option value="OUT_OF_STOCK">Nicht auf Lager</option>
              </select>
            </label>

            <label className="form-label">
              Lieferzeit (Tage)
              <input
                type="number"
                min="1"
                max="5"
                className={`form-control ${
                  newInvalid.shippingTime ? "is-invalid" : ""
                }`}
                placeholder="1-5"
                value={newProductForm.shippingTime}
                onChange={(e) =>
                  handleNewChange("shippingTime", parseInt(e.target.value))
                }
              />
              {newInvalid.shippingTime && (
                <div className="invalid-feedback">
                  Lieferzeit muss 1-5 Tage sein
                </div>
              )}
            </label>

            <label className="form-label d-flex align-items-center column-gap-2">
              <input
                type="checkbox"
                checked={newProductForm.active}
                onChange={(e) => handleNewChange("active", e.target.checked)}
              />
              Aktiv
            </label>

            <div className="mt-3">
              <label className="form-label fw-bold">Bilder hinzufügen</label>
              <input
                type="file"
                multiple
                accept="image/*"
                onChange={handleImageChange}
                style={{ display: "none" }}
                id="product-images-input"
              />
              <button
                type="button"
                className="btn btn-primary mb-2"
                onClick={() =>
                  document.getElementById("product-images-input")?.click()
                }
              >
                Bilder auswählen
              </button>

              <div className="d-flex flex-wrap gap-2">
                {uploadedImages.map((file, index) => {
                  const url = URL.createObjectURL(file);
                  return (
                    <div
                      key={index}
                      className="position-relative"
                      style={{
                        width: 100,
                        height: 100,
                        border: "1px solid #ccc",
                        borderRadius: 4,
                        overflow: "hidden",
                      }}
                    >
                      <img
                        src={url}
                        alt={file.name}
                        style={{
                          width: "100%",
                          height: "100%",
                          objectFit: "cover",
                        }}
                      />
                      <button
                        type="button"
                        onClick={() => removeImage(index)}
                        className="btn btn-sm btn-danger position-absolute"
                        style={{ top: 2, right: 2, padding: "0 4px" }}
                      >
                        ×
                      </button>
                    </div>
                  );
                })}
              </div>
            </div>
          </div>

          <div className="d-flex column-gap-2 mt-3">
            <button className="btn btn-success" onClick={saveNewProduct}>
              Speichern
            </button>
            <button
              className="btn btn-secondary"
              onClick={() => setShowNewForm(false)}
            >
              Abbrechen
            </button>
          </div>
        </div>
      )}

      <table className="table table-bordered table-striped">
        <thead>
          <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Kategorie</th>
            <th>Beschreibung</th>
            <th>Preis</th>
            <th>Versandkosten</th>
            <th>Marke</th>
            <th>Lagerstatus</th>
            <th>Lieferzeit</th>
            <th>Aktiv</th>
            <th>Aktionen</th>
          </tr>
        </thead>
        <tbody>
          {products.map((product) => (
            <FullProduct
              key={product.id}
              product={product}
              isSelected={selectedProductId === product.id.toString()}
              onSelect={() => handleSelectedProduct(product.id.toString())}
              onUpdate={handleProductUpdated}
              onDelete={handleProductDeleted}
            />
          ))}
        </tbody>
      </table>
    </div>
  );
}
