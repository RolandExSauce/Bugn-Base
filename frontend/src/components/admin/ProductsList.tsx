import { useEffect, useRef, useState } from "react";
import FullProduct from "../../components/admin/FullProduct";
import type { Product } from "../../types/models";
import { NAME_REGEX, PRICE_REGEX } from "../../utils/regex";
import AdminService from "../../services/admin/admin.order.service";
import { mockProducts } from "../../api/mock";

export default function ProductsList() {
  const [selectedProductId, setSelectedProductId] = useState<string>("");

  const divRef = useRef<HTMLDivElement>(null);

  // later remove the dummy data and instead use below:
  const [products, setProducts] = useState(
    Array.from({ length: 10 }, (_, i) => ({
      ...mockProducts,
      id: String(i + 1),
    }))
  );

  // const fetchProducts = async () => {
  //   try {
  //     const data = await AdminService.getProducts();
  //     setProducts(data);
  //   } catch (error) {
  //     console.error(error);
  //   }
  // };

  // useEffect(() => {
  //   fetchProducts();
  // }, []);

  const [showNewForm, setShowNewForm] = useState(false);

  const [newProductForm, setNewProductForm] = useState<Partial<Product>>({
    name: "",
    category: "piano" as Product["category"],
    description: "",
    price: 0,
    shippingCost: 0,
    brand: "",
    stockStatus: true,
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

  const handleSelectedProduct = (id: string) => {
    if (selectedProductId === id) return;
    setSelectedProductId(id);
  };

  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  const handleNewChange = (key: keyof Product, value: any) => {
    setNewProductForm((prev) => ({ ...prev, [key]: value }));
  };

  const saveNewProduct = () => {
    const nextInvalid = {
      name: false,
      description: false,
      price: false,
      shippingCost: false,
      brand: false,
      shippingTime: false,
    };

    let hasError = false;

    if (!NAME_REGEX.test(newProductForm.name ?? "")) {
      nextInvalid.name = true;
      hasError = true;
    }

    if (!PRICE_REGEX.test(String((newProductForm.price ?? 0) * 100))) {
      nextInvalid.price = true;
      hasError = true;
    }

    if (!PRICE_REGEX.test(String((newProductForm.shippingCost ?? 0) * 100))) {
      nextInvalid.shippingCost = true;
      hasError = true;
    }

    if (!NAME_REGEX.test(newProductForm.brand ?? "")) {
      nextInvalid.brand = true;
      hasError = true;
    }

    if (!PRICE_REGEX.test(String(newProductForm.shippingTime))) {
      nextInvalid.shippingTime = true;
      hasError = true;
    }

    setNewInvalid(nextInvalid);

    if (hasError) return;

    const productToAdd: Product = {
      id: 0,
      name: newProductForm.name ?? "",
      category: newProductForm.category as Product["category"],
      description: newProductForm.description ?? "",
      price: Number(newProductForm.price),
      shippingCost: Number(newProductForm.shippingCost),
      brand: newProductForm.brand ?? "",
      stockStatus: newProductForm.stockStatus ?? "IN_STOCK",
      shippingTime: Number(newProductForm.shippingTime),
      active: newProductForm.active ?? false,
    };

    try {
      // AdminService.addProduct(productToAdd)

      // success animation
      divRef.current?.classList.remove("success-animation");
      void divRef.current?.offsetWidth;
      divRef.current?.classList.add("success-animation");

      // reset states:
      setTimeout(() => {
        setShowNewForm(false);
        setNewProductForm({
          name: "",
          category: "PIANO",
          description: "",
          price: 0,
          shippingCost: 0,
          brand: "",
          stockStatus: "LOW_STOCK",
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
      console.log(error);
      // todo: ipmlement error handling in the ui
    }
  };

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
                <div className="text-danger">Name ist ungültig</div>
              )}
            </label>

            <label className="form-label">
              Kategorie
              <select
                className="form-select"
                value={newProductForm.category}
                onChange={(e) =>
                  handleNewChange(
                    "category",
                    e.target.value as Product["category"]
                  )
                }
              >
                <option value="piano">Klavier</option>
                <option value="guitar">Gitarre</option>
                <option value="violin">Violine</option>
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
              Preis
              <input
                type="number"
                className="form-control"
                placeholder="0"
                value={newProductForm.price ? newProductForm.price / 100 : 1}
                onChange={(e) =>
                  handleNewChange("price", Number(e.target.value) * 100)
                }
              />
              {newInvalid.price && (
                <div className="text-danger">Preis ist ungültig</div>
              )}
            </label>

            <label className="form-label">
              Versandkosten
              <input
                type="number"
                className="form-control"
                placeholder="0"
                value={newProductForm.shippingCost}
                onChange={(e) =>
                  handleNewChange("shippingCost", Number(e.target.value))
                }
              />
              {newInvalid.shippingCost && (
                <div className="text-danger">Versandkosten sind ungültig</div>
              )}
            </label>

            <label className="form-label">
              Marke
              <input
                type="text"
                className="form-control"
                placeholder="Markenname"
                value={newProductForm.brand}
                onChange={(e) => handleNewChange("brand", e.target.value)}
              />
              {newInvalid.brand && (
                <div className="text-danger">Marke ist ungültig</div>
              )}
            </label>

            <label className="form-label">
              Lagerstatus
              <select
                className="form-select"
                value={newProductForm.stockStatus ? "in" : "out"}
                onChange={(e) =>
                  handleNewChange("stockStatus", e.target.value === "in")
                }
              >
                <option value="in">Auf Lager</option>
                <option value="out">Nicht auf Lager</option>
              </select>
            </label>

            <label className="form-label">
              Lieferzeit (Tage)
              <input
                type="number"
                className="form-control"
                placeholder="0"
                value={newProductForm.shippingTime}
                onChange={(e) =>
                  handleNewChange("shippingTime", Number(e.target.value))
                }
              />
              {newInvalid.shippingTime && (
                <div className="text-danger">Lieferzeit ist ungültig</div>
              )}
            </label>

            <label className="form-label d-flex align-items-center column-gap-2">
              Aktiv
              <input
                type="checkbox"
                checked={newProductForm.active}
                onChange={(e) => handleNewChange("active", e.target.checked)}
              />
            </label>
          </div>

          <div className="d-flex column-gap-2 mt-3">
            <button className="btn btn-success" onClick={saveNewProduct}>
              Speichern
            </button>
            <button className="btn btn-secondary" onClick={saveNewProduct}>
              Abbrechen
            </button>
          </div>
        </div>
      )}

      <table className="table table-bordered table-striped">
        <thead>
          <tr>
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
              handleSelect={handleSelectedProduct}
              selectedProductId={selectedProductId}
              key={product.id}
              initialProduct={product}
            />
          ))}
        </tbody>
      </table>
    </div>
  );
}
