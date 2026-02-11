import { useState, useEffect, useRef } from "react";
import type { Role, User } from "../../types/models";
import AdminUserService from "../../services/admin.user.service";
import AdminDeleteButton from "../common/AdminDeleteButton";
import AdminUpdateButton from "../common/AdminUpdateButton";
import AdminSelectRowButton from "../common/AdminSelectRowButton";
import {
  ADDRESS_REGEX,
  EMAIL_REGEX,
  NAME_REGEX,
  PHONE_REGEX,
  POSTCODE_REGEX,
} from "../../utils/regex";

interface UserProps {
  user: User;
  handleSelect: (id: string) => void;
  selectedUserId: string | null;
  onUpdated: (user: User) => void;
  onDeleted: (id: string) => void;
}

type AdminUpdateUserDto = {
  firstname: string;
  lastname: string;
  phone?: string | number;
  address?: string;
  postcode: number;
  email: string;
  active: boolean;
  role: Role; // "ROLE_USER" | "ROLE_ADMIN"
};

const UserRow = ({
  user,
  handleSelect,
  selectedUserId,
  onUpdated,
  onDeleted,
}: UserProps) => {
  const [form, setForm] = useState<User>(user);
  const [isEdited, setIsEdited] = useState(false);
  const [isSaving, setIsSaving] = useState(false);
  const [isDeleting, setIsDeleting] = useState(false);

  const trRef = useRef<HTMLTableRowElement>(null);

  useEffect(() => setForm(user), [user]);

  const [invalidInput, setInvalidInput] = useState({
    firstname: false,
    lastname: false,
    phone: false,
    address: false,
    postcode: false,
    email: false,
  });

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;
    setIsEdited(true);

    if (e.target instanceof HTMLInputElement && e.target.type === "checkbox") {
      setForm({ ...form, [name]: e.target.checked });
    } else if (name === "postcode") {
      setForm({ ...form, [name]: Number(value) });
    } else {
      setForm({ ...form, [name]: value });
    }
  };

  const validate = () => {
    const newInvalidInput = {
      firstname: false,
      lastname: false,
      phone: false,
      address: false,
      postcode: false,
      email: false,
    };
    let invalidInputs = false;

    if (!NAME_REGEX.test(form.firstname)) {
      newInvalidInput.firstname = true;
      invalidInputs = true;
    }
    if (!NAME_REGEX.test(form.lastname)) {
      newInvalidInput.lastname = true;
      invalidInputs = true;
    }

    // phone kann optional sein → nur validieren wenn vorhanden
    if (form.phone !== undefined && form.phone !== null && String(form.phone).trim() !== "") {
      if (!PHONE_REGEX.test(String(form.phone))) {
        newInvalidInput.phone = true;
        invalidInputs = true;
      }
    }

    if (!ADDRESS_REGEX.test(form.address ?? "")) {
      newInvalidInput.address = true;
      invalidInputs = true;
    }
    if (!POSTCODE_REGEX.test(String(form.postcode))) {
      newInvalidInput.postcode = true;
      invalidInputs = true;
    }
    if (!EMAIL_REGEX.test(form.email)) {
      newInvalidInput.email = true;
      invalidInputs = true;
    }

    setInvalidInput(newInvalidInput);
    return !invalidInputs;
  };

  const handleSave = async () => {
    if (!validate()) return;

    const dto: AdminUpdateUserDto = {
      firstname: form.firstname,
      lastname: form.lastname,
      phone: form.phone,
      address: form.address,
      postcode: Number(form.postcode),
      email: form.email,
      active: form.active,
      role: form.role,
    };

    try {
      setIsSaving(true);
      const updated = await AdminUserService.updateUser(form.id, dto);

      // UI feedback
      trRef.current?.classList.remove("user-row-success");
      void trRef.current?.offsetWidth;
      trRef.current?.classList.add("user-row-success");

      setIsEdited(false);
      handleSelect("");
      onUpdated(updated);
    } catch (err) {
      console.error("Error updating user:", err);
      alert("User konnte nicht gespeichert werden.");
    } finally {
      setIsSaving(false);
    }
  };

  const handleDelete = async () => {
    const ok = confirm("Benutzer wirklich löschen/deaktivieren?");
    if (!ok) return;

    try {
      setIsDeleting(true);
      await AdminUserService.deleteUser(form.id);
      onDeleted(form.id);
    } catch (err) {
      console.error("Error deleting user:", err);
      alert("User konnte nicht gelöscht/deaktiviert werden.");
    } finally {
      setIsDeleting(false);
    }
  };

  const handleUndoEdit = () => {
    handleSelect("");
    setForm(user);
    setIsEdited(false);
    setInvalidInput({
      firstname: false,
      lastname: false,
      phone: false,
      address: false,
      postcode: false,
      email: false,
    });
  };

  // Editable row
  if (selectedUserId === form.id) {
    return (
      <tr key={form.id} ref={trRef} className="editable-user-row">
        <td>{form.id}</td>

        <td>
          <input
            type="text"
            name="firstname"
            value={form.firstname}
            onChange={handleChange}
          />
          {invalidInput.firstname && (
            <span className="d-block text-danger invalid-input">
              Ungültiger Vorname
            </span>
          )}
        </td>

        <td>
          <input
            type="text"
            name="lastname"
            value={form.lastname}
            onChange={handleChange}
          />
          {invalidInput.lastname && (
            <span className="d-block text-danger invalid-input">
              Ungültiger Nachname
            </span>
          )}
        </td>

        <td>
          <input
            type="tel"
            name="phone"
            value={form.phone ?? ""}
            onChange={handleChange}
          />
          {invalidInput.phone && (
            <span className="d-block text-danger invalid-input">
              Ungültige Telefonnummer
            </span>
          )}
        </td>

        <td>
          <input
            type="text"
            name="address"
            value={form.address ?? ""}
            onChange={handleChange}
          />
          {invalidInput.address && (
            <span className="d-block text-danger invalid-input">
              Ungültige Adresse
            </span>
          )}
        </td>

        <td>
          <input
            type="number"
            name="postcode"
            value={form.postcode}
            onChange={handleChange}
          />
          {invalidInput.postcode && (
            <span className="d-block text-danger invalid-input">
              Ungültige Postleitzahl
            </span>
          )}
        </td>

        <td>
          <input
            type="email"
            name="email"
            value={form.email}
            onChange={handleChange}
          />
          {invalidInput.email && (
            <span className="d-block text-danger invalid-input">
              Ungültige E-Mail
            </span>
          )}
        </td>

        <td>
          <input
            type="checkbox"
            name="active"
            checked={form.active}
            onChange={handleChange}
          />
        </td>

        <td>
          <select name="role" value={form.role} onChange={handleChange}>
            <option value="ROLE_USER">USER</option>
            <option value="ROLE_ADMIN">ADMIN</option>
          </select>
        </td>

        <td>
          <AdminUpdateButton
            disabled={!isEdited || isSaving}
            action={handleSave}
          />
          <button
            className="admin-user-action-button"
            onClick={handleUndoEdit}
            disabled={isSaving}
          >
            <img
              width="25px"
              height="25px"
              src="/undo.svg"
              alt="Undo user edit button icon"
            />
          </button>

          <button
            className="admin-user-action-button"
            onClick={handleDelete}
            disabled={isDeleting || isSaving}
            title="Löschen/Deaktivieren"
          >
            {isDeleting ? "..." : "✕"}
          </button>
        </td>
      </tr>
    );
  }

  // Readonly row
  return (
    <tr key={form.id}>
      <td>{form.id}</td>
      <td>{form.firstname}</td>
      <td>{form.lastname}</td>
      <td>{form.phone ?? ""}</td>
      <td>{form.address ?? ""}</td>
      <td>{form.postcode}</td>
      <td>{form.email}</td>
      <td>
        <input type="checkbox" disabled checked={form.active} readOnly />
      </td>
      <td>
        <select disabled name="role" value={form.role} onChange={handleChange}>
          <option value="ROLE_USER">USER</option>
          <option value="ROLE_ADMIN">ADMIN</option>
        </select>
      </td>
      <td>
        <AdminSelectRowButton action={() => handleSelect(form.id)} />
        <AdminDeleteButton action={handleDelete} />
      </td>
    </tr>
  );
};

export default UserRow;
