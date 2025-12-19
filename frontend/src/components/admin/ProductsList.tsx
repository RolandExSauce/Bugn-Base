import { useEffect, useRef, useState } from "react";
import FullProduct from "../../components/admin/FullProduct";
import type { Product, ProductDTO, Image } from "../../types/models";
import { AdminProductService } from "../../services";

export default function ProductsList() {
  const [selectedProductId, setSelectedProductId] = useState<string>("");
  const divRef = useRef<HTMLDivElement>(null);
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [showNewForm, setShowNewForm] = useState(false);
  const [uploadedImages, setUploadedImages] = useState<File[]>([]);
  const [imageUploading, setImageUploading] = useState(false);

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

  useEffect(() => {
    fetchProducts();
  }, []);

  const handleSelectedProduct = (id: string) => {
    setSelectedProductId(selectedProductId === id ? "" : id);
  };

  const handleNewChange = (key: keyof ProductDTO, value: any) => {
    setNewProductForm((prev) => ({ ...prev, [key]: value }));
  };

  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (!e.target.files) return;
    const filesArray = Array.from(e.target.files);
    setUploadedImages((prev) => [...prev, ...filesArray]);
  };

  const removeImage = (index: number) => {
    setUploadedImages((prev) => prev.filter((_, i) => i !== index));
  };

  const saveNewProduct = async () => {
    const invalids = {
      name: !newProductForm.name.trim(),
      description: !newProductForm.description.trim(),
      price: !(newProductForm.price > 0),
      shippingCost: !(newProductForm.shippingCost >= 0),
      brand: !newProductForm.brand.trim(),
      shippingTime: !(
        newProductForm.shippingTime >= 1 && newProductForm.shippingTime <= 5
      ),
    };

    setNewInvalid(invalids);

    if (Object.values(invalids).some(Boolean)) return;

    try {
      const savedProduct = await AdminProductService.addProduct(newProductForm);

      if (uploadedImages.length > 0) {
        await uploadImagesForProduct(savedProduct);
      }

      setProducts((prev) => [...prev, savedProduct]);
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
      setUploadedImages([]);
      setNewInvalid({
        name: false,
        description: false,
        price: false,
        shippingCost: false,
        brand: false,
        shippingTime: false,
      });
    } catch (err) {
      console.error("Error adding product:", err);
    }
  };

  const uploadImagesForProduct = async (product: Product) => {
    setImageUploading(true);
    for (const file of uploadedImages) {
      try {
        const url = await AdminProductService.addImage(product, file);
        // product.images.push({ imageId: "", url });
      } catch (err) {
        console.error(`Failed to upload image ${file.name}`, err);
      }
    }
    setImageUploading(false);
    setUploadedImages([]);
  };

  const handleDeleteImage = async (product: Product, image: Image) => {
    if (!window.confirm("Bild wirklich löschen?")) return;
    try {
      await AdminProductService.deleteImage(image.url);
      product.images = product.images.filter(
        (i) => i.imageId !== image.imageId
      );
      setProducts([...products]);
    } catch (err) {
      console.error("Error deleting image:", err);
    }
  };

  const resolveImageUrl = (url?: string) => {
    if (!url) return "/no_found_placeholder.jpg";
    if (url.includes("/media/")) {
      return `${import.meta.env.VITE_BASE_URL}${url}`;
    }
    return `${import.meta.env.VITE_BASE_URL}/media${url}`;
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
        <div className="spinner-border text-primary" role="status" />
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
          <div className="mb-3">
            <label className="form-label">Name</label>
            <input
              type="text"
              className={`form-control ${newInvalid.name ? "is-invalid" : ""}`}
              value={newProductForm.name}
              onChange={(e) => handleNewChange("name", e.target.value)}
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Kategorie</label>
            <select
              className="form-control"
              value={newProductForm.category}
              onChange={(e) => handleNewChange("category", e.target.value)}
            >
              <option value="GUITARS">Gitarre</option>
              <option value="PIANOS">Klavier</option>
              <option value="VIOLINS">Violine</option>
            </select>
          </div>

          <div className="mb-3">
            <label className="form-label">Marke</label>
            <input
              type="text"
              className={`form-control ${newInvalid.brand ? "is-invalid" : ""}`}
              value={newProductForm.brand}
              onChange={(e) => handleNewChange("brand", e.target.value)}
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Beschreibung</label>
            <textarea
              className={`form-control ${
                newInvalid.description ? "is-invalid" : ""
              }`}
              rows={3}
              value={newProductForm.description}
              onChange={(e) => handleNewChange("description", e.target.value)}
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Preis (€)</label>
            <input
              type="number"
              step="0.01"
              className={`form-control ${newInvalid.price ? "is-invalid" : ""}`}
              value={newProductForm.price}
              onChange={(e) =>
                handleNewChange("price", parseFloat(e.target.value))
              }
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Versandkosten (€)</label>
            <input
              type="number"
              step="0.01"
              className={`form-control ${
                newInvalid.shippingCost ? "is-invalid" : ""
              }`}
              value={newProductForm.shippingCost}
              onChange={(e) =>
                handleNewChange("shippingCost", parseFloat(e.target.value))
              }
            />
          </div>

          <div className="mb-3">
            <label className="form-label">Lagerbestand</label>
            <select
              className="form-control"
              value={newProductForm.stockStatus}
              onChange={(e) => handleNewChange("stockStatus", e.target.value)}
            >
              <option value="IN_STOCK">Auf Lager</option>
              <option value="LOW_STOCK">Gering</option>
              <option value="OUT_OF_STOCK">Nicht auf Lager</option>
            </select>
          </div>

          <div className="mb-3">
            <label className="form-label">Versandzeit (Tage)</label>
            <input
              type="number"
              min={1}
              max={5}
              className={`form-control ${
                newInvalid.shippingTime ? "is-invalid" : ""
              }`}
              value={newProductForm.shippingTime}
              onChange={(e) =>
                handleNewChange("shippingTime", parseInt(e.target.value))
              }
            />
          </div>

          <div className="form-check mb-3">
            <input
              type="checkbox"
              className="form-check-input"
              checked={newProductForm.active}
              onChange={(e) => handleNewChange("active", e.target.checked)}
            />
            <label className="form-check-label">Aktiv</label>
          </div>
          <input
            type="file"
            multiple
            accept="image/*"
            onChange={handleImageChange}
            style={{ display: "none" }}
            id="product-images-input"
          />
          <button
            className="btn btn-primary mb-2"
            onClick={() =>
              document.getElementById("product-images-input")?.click()
            }
          >
            Bilder auswählen
          </button>
          <div className="d-flex flex-wrap gap-2 mb-3">
            {uploadedImages.map((file, index) => (
              <div
                key={index}
                style={{ position: "relative", width: 100, height: 100 }}
              >
                <img
                  src={URL.createObjectURL(file)}
                  style={{ width: "100%", height: "100%", objectFit: "cover" }}
                />
                <button
                  onClick={() => removeImage(index)}
                  style={{
                    position: "absolute",
                    top: 0,
                    right: 0,
                    padding: "0 4px",
                  }}
                  className="btn btn-sm btn-danger"
                >
                  ×
                </button>
              </div>
            ))}
          </div>
          <button className="btn btn-success" onClick={saveNewProduct}>
            Speichern
          </button>
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
            <th>Lagerbestand</th>
            <th>Versandzeit</th>
            <th>Aktiv</th>
            <th>Aktionen</th>
          </tr>
        </thead>
        <tbody>
          {products.map((product) => (
            <>
              <FullProduct
                key={product.id}
                product={product}
                isSelected={selectedProductId === product.id.toString()}
                onSelect={() => handleSelectedProduct(product.id.toString())}
                onUpdate={handleProductUpdated}
                onDelete={handleProductDeleted}
              />
              {/* Images row - show for all products */}
              {product.images && product.images.length > 0 && (
                <tr key={`${product.id}-images`}>
                  <td colSpan={11}>
                    <div className="p-2">
                      <strong>Produktbilder:</strong>
                      <div className="d-flex flex-wrap gap-2 mt-2">
                        {product.images.map((img) => (
                          <div
                            key={img.imageId}
                            style={{
                              position: "relative",
                              width: 80,
                              height: 80,
                            }}
                          >
                            <img
                              src={resolveImageUrl(img.url)}
                              style={{
                                width: "100%",
                                height: "100%",
                                objectFit: "cover",
                                borderRadius: 4,
                              }}
                              alt="Product"
                            />
                            <button
                              className="btn btn-sm btn-danger position-absolute top-0 end-0"
                              onClick={() => handleDeleteImage(product, img)}
                              style={{ padding: "0 4px" }}
                            >
                              ×
                            </button>
                          </div>
                        ))}
                      </div>
                    </div>
                  </td>
                </tr>
              )}
            </>
          ))}
        </tbody>
      </table>
    </div>
  );
}
