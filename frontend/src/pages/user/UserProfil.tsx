import { useEffect, useRef, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Order } from "../../components/order/Order";
import type { Order as OrderDTO, User, MessageDto } from "../../types/models";
import {
  ADDRESS_REGEX,
  EMAIL_REGEX,
  NAME_REGEX,
  PHONE_REGEX,
  POSTCODE_REGEX,
} from "../../utils/regex";
import { useAuthContext } from "../../context/AuthContext";
import UserOrderService from "../../services/user.order.service";
import UserProfileService from "../../services/user.profile.service";
import MessageService from "../../services/message.service";

const UserProfil = () => {
  const { auth, logout } = useAuthContext();

  const [isEdited, setIsEdited] = useState(false);
  const [orders, setOrders] = useState<OrderDTO[]>([]);
  const [inboxCount, setInboxCount] = useState(0);

  const navigate = useNavigate();

  const formRef = useRef<HTMLFormElement>(null);
  const [userProfileForm, setUserProfileForm] = useState<Partial<User>>({
    firstname: "",
    lastname: "",
    phone: undefined,
    address: undefined,
    postcode: undefined,
    email: "",
  });

  const [invalidInput, setInvalidInput] = useState({
    firstname: false,
    lastname: false,
    phone: false,
    address: false,
    postcode: false,
    email: false,
    password: false,
  });

  useEffect(() => {
    if (!auth) return;

    setUserProfileForm({
      firstname: auth.user.firstname ?? "",
      lastname: auth.user.lastname ?? "",
      phone: auth.user.phone ?? undefined,
      address: auth.user.address ?? undefined,
      postcode: auth.user.postcode ?? undefined,
      email: auth.user.email ?? "",
    });

    // Orders
    UserOrderService.getOrdersForCustomer()
      .then((data) => setOrders(data))
      .catch((err) => console.error("Error fetching orders:", err));

    // Inbox counter (nur beantwortete Nachrichten)
    if (auth.role === "ROLE_USER") {
      MessageService.getInboxUnreadCount()
        .then(setInboxCount)
        .catch((err) => console.error("Error fetching unread count:", err));
    }
  }, [auth]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setIsEdited(true);
    setUserProfileForm({
      ...userProfileForm,
      [e.target.name]: e.target.value,
    });
  };

  const handleSaveUserDetails = async (e: React.FormEvent) => {
    e.preventDefault();

    const newInvalidInput = {
      firstname: false,
      lastname: false,
      phone: false,
      address: false,
      postcode: false,
      email: false,
      password: false,
    };

    let hasError = false;

    if (!NAME_REGEX.test(userProfileForm.firstname ?? "")) {
      newInvalidInput.firstname = true;
      hasError = true;
    }
    if (!NAME_REGEX.test(userProfileForm.lastname ?? "")) {
      newInvalidInput.lastname = true;
      hasError = true;
    }
    if (
      userProfileForm.phone &&
      !PHONE_REGEX.test(String(userProfileForm.phone ?? ""))
    ) {
      newInvalidInput.phone = true;
      hasError = true;
    }
    if (
      userProfileForm.address &&
      !ADDRESS_REGEX.test(userProfileForm.address ?? "")
    ) {
      newInvalidInput.address = true;
      hasError = true;
    }
    if (
      userProfileForm.address &&
      userProfileForm.postcode &&
      !POSTCODE_REGEX.test(String(userProfileForm.postcode ?? ""))
    ) {
      newInvalidInput.postcode = true;
      hasError = true;
    }
    if (!EMAIL_REGEX.test(userProfileForm.email ?? "")) {
      newInvalidInput.email = true;
      hasError = true;
    }

    setInvalidInput(newInvalidInput);
    if (hasError) return;

    try {
      const updatedUser: User = await UserProfileService.updateUserProfile({
        id: auth!.user.id,
        ...userProfileForm,
      });

      formRef.current?.classList.remove("success-animation");
      void formRef.current?.offsetWidth;
      formRef.current?.classList.add("success-animation");

      setIsEdited(false);
      auth!.user = updatedUser;
    } catch (error) {
      console.log(error);
      // TODO: handle error in the ui
    }
  };

  return (
    <div className="d-flex flex-column container py-4">
      <h1 className="mb-4">Mein Profil</h1>

      <form
        ref={formRef}
        className="border rounded p-3 mb-4 d-flex flex-column gap-3"
        onSubmit={handleSaveUserDetails}
      >
        <div className="fw-bold">Persönliche Daten:</div>

        <div className="d-flex flex-row gap-3">
          <div className="flex-fill">
            <label className="form-label">Vorname</label>
            <input
              type="text"
              className="form-control"
              name="firstname"
              value={userProfileForm.firstname ?? ""}
              onChange={handleChange}
              required
            />
          </div>

          <div className="flex-fill">
            <label className="form-label">Nachname</label>
            <input
              type="text"
              className="form-control"
              name="lastname"
              value={userProfileForm.lastname ?? ""}
              onChange={handleChange}
              required
            />
          </div>
        </div>

        <div className="d-flex gap-3 flex-row">
          <div className="flex-fill">
            <label className="form-label">E-Mail</label>
            <input
              type="text"
              className="form-control"
              name="email"
              value={userProfileForm.email ?? ""}
              onChange={handleChange}
              required
            />
          </div>

          <div className="flex-fill">
            <label className="form-label">Telefon</label>
            <input
              type="tel"
              className="form-control"
              name="phone"
              value={userProfileForm.phone ?? ""}
              onChange={handleChange}
            />
          </div>
        </div>

        <div className="d-flex gap-3 flex-row">
          <div className="flex-fill">
            <label className="form-label">Adresse</label>
            <input
              type="text"
              className="form-control"
              name="address"
              value={userProfileForm.address ?? ""}
              onChange={handleChange}
            />
          </div>

          <div className="flex-fill">
            <label className="form-label">PLZ</label>
            <input
              type="number"
              className="form-control"
              name="postcode"
              value={userProfileForm.postcode ?? ""}
              onChange={handleChange}
            />
          </div>
        </div>

        <div className="d-flex flex-column align-items-end">
          <button
            aria-label="Save"
            type="submit"
            className={`profile-save-button text-white px-4 py-2 fw-bold h4 ${
              isEdited ? "" : "button-disabled"
            }`}
            onClick={handleSaveUserDetails}
            disabled={!isEdited}
          >
            Speichern
          </button>

          {invalidInput.firstname && (
            <p className="text-danger">Gultiger Vorname ist erforderlich</p>
          )}
          {invalidInput.lastname && (
            <p className="text-danger">Gültiger Nachname ist erforderlich</p>
          )}
          {invalidInput.email && (
            <p className="text-danger">Gültiger E-Mail ist erforderlich</p>
          )}
          {invalidInput.phone && (
            <p className="text-danger">Telefon ist ungültig</p>
          )}
          {invalidInput.address && (
            <p className="text-danger">Adresse ist ungültig</p>
          )}
          {invalidInput.postcode && (
            <p className="text-danger">
              PLZ ist ungültig oder Adresse ist leer
            </p>
          )}

          {auth?.role === "ROLE_ADMIN" && (
            <Link
              to="/admin"
              className="profile-save-button bg-success rounded text-white px-4 py-2 fw-bold h4"
            >
              zur Adminseite
            </Link>
          )}

          {auth?.role === "ROLE_USER" && (
            <button
              aria-label="My messages"
              type="button"
              onClick={() => navigate("/my-messages")}
              className="profile-save-button bg-primary rounded text-white px-4 py-2 fw-bold h4 d-flex align-items-center gap-2"
            >
              Meine Nachrichten
              {inboxCount > 0 && (
                <span className="badge bg-light text-dark">{inboxCount}</span>
              )}
            </button>
          )}

          <button
            aria-label="Logout"
            onClick={logout}
            type="button"
            className="profile-save-button bg-danger rounded text-white px-4 py-2 fw-bold h4"
          >
            Abmelden
          </button>
        </div>
      </form>

      <div className="border rounded p-3 d-flex flex-column gap-3">
        <div className="fw-bold">Meine Bestellungen:</div>
        <div id="cart-items" className="d-flex flex-column gap-2">
          {orders.map((order, i) => (
            <Order key={i} order={order} />
          ))}
        </div>
      </div>
    </div>
  );
};

export default UserProfil;
