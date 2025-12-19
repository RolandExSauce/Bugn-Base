import { useEffect, useRef, useState } from "react";
import FullProduct from "../../components/admin/FullProduct";
import type { Product, ProductDTO, Image } from "../../types/models";
import { noImgFoundPlaceholder } from "../../assets/icon.barrel";
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
      description: false,
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
      await AdminProductService.deleteImage(image.imageId);
      product.images = product.images.filter(
        (i) => i.imageId !== image.imageId
      );
      setProducts([...products]);
    } catch (err) {
      console.error("Error deleting image:", err);
    }
  };

  const resolveImageUrl = (url?: string) => {
    if (!url) return noImgFoundPlaceholder;
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
          {/* Product Fields ... same as before ... */}
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
            <th>Preis</th>
            <th>Aktionen</th>
          </tr>
        </thead>
        <tbody>
          {products.map((product) => (
            <tr key={product.id}>
              <td>{product.id}</td>
              <td>{product.name}</td>
              <td>{product.category}</td>
              <td>{product.price}</td>
              <td>
                <FullProduct
                  product={product}
                  isSelected={selectedProductId === product.id.toString()}
                  onSelect={() => handleSelectedProduct(product.id.toString())}
                  onUpdate={handleProductUpdated}
                  onDelete={handleProductDeleted}
                />
                {/* Images UI */}
                <div className="d-flex flex-wrap gap-2 mt-2">
                  {product.images.map((img) => (
                    <div
                      key={img.imageId}
                      style={{ position: "relative", width: 80, height: 80 }}
                    >
                      <img
                        src={resolveImageUrl(img.url)}
                        style={{
                          width: "100%",
                          height: "100%",
                          objectFit: "cover",
                          borderRadius: 4,
                        }}
                      />
                      <button
                        className="btn btn-sm btn-danger position-absolute top-0 end-0"
                        onClick={() => handleDeleteImage(product, img)}
                      >
                        ×
                      </button>
                    </div>
                  ))}
                </div>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
