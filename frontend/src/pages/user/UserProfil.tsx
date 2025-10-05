import { Order } from "../../components/order/Order";

const UserProfil = () => {
  return (
    <div className="d-flex flex-column container py-4">
      <h1 className="mb-4">Mein Profil</h1>

      <form className="border rounded p-3 mb-4 d-flex flex-column gap-3">
        <div className="fw-bold">Persönliche Daten:</div>

        <div className="d-flex flex-row gap-3">
          <input type="text" className="form-control" placeholder="Vorname" />
          <input type="text" className="form-control" placeholder="Nachname" />
        </div>

        <div className="d-flex gap-3 flex-row">
          <input type="email" className="form-control" placeholder="Email" />
          <input type="tel" className="form-control" placeholder="Tel" />
        </div>

        <div className="d-flex gap-3 flex-row">
          <input type="text" className="form-control" placeholder="Adresse" />
          <input type="text" className="form-control" placeholder="PLZ" />
        </div>
        <div className="d-flex justify-content-end">
          <button className="profile-save-button text-white px-4 py-2 fw-bold h4">
            Speichern
          </button>
        </div>
      </form>

      <div className="border rounded p-3 d-flex flex-column gap-3">
        <div className="fw-bold">Meine Bestellungen:</div>

        <div id="cart-items" className="d-flex flex-column gap-2">
          <Order />
          <Order />
          <Order />
        </div>
      </div>
    </div>
  );
};
export default UserProfil;
