import { useState, useEffect, useRef } from "react";
import type { User } from "../../types/models";
import AdminService from "../../services/admin/admin.service";
import AdminDeleteButton from "../common/AdminDeleteButton";
import AdminUpdateButton from "../common/AdminUpdateButton";
import AdminSelectRowButton from "../common/AdminSelectRowButton";
import {
  ADDRESS_REGEX,
  EMAIL_REGEX,
  NAME_REGEX,
  PHONE_REGEX,
  POSTCODE_REGEX,
} from "../../types/regex";

interface UserProps {
  user: User;
  handleSelect: (id: string) => void;
  selectedUserId: string | null;
}

const UserRow = ({ user, handleSelect, selectedUserId }: UserProps) => {
  const [form, setForm] = useState<User>(user);
  const [isEdited, setIsEdited] = useState(false);
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
    } else {
      setForm({ ...form, [name]: value });
    }
  };

  const handleSave = () => {
    // todo: "do you want to save"

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
    if (!PHONE_REGEX.test(String(form.phone))) {
      newInvalidInput.phone = true;
      invalidInputs = true;
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

    if (invalidInputs) {
      setInvalidInput(newInvalidInput);
      return;
    }

    // API call
    // AdminService.updateUser(form);

    setInvalidInput({
      firstname: false,
      lastname: false,
      phone: false,
      address: false,
      postcode: false,
      email: false,
    });

    trRef.current?.classList.remove("user-row-success");
    void trRef.current?.offsetWidth;
    trRef.current?.classList.add("user-row-success");
    setTimeout(() => {
      handleSelect("");
      setIsEdited(false);
    }, 800);
  };

  const handleDelete = () => {
    // first confirm, do you really want to delete
    AdminService.deleteUser(form.id);
  };

  const handleUndoEdit = () => {
    handleSelect("");
    setForm(user);
  };

  // render editable row if the row is selected
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
            value={form.phone}
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
            value={form.address}
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
            <option value="user">USER</option>
            <option value="admin">ADMIN</option>
          </select>
        </td>
        <td>
          <AdminUpdateButton disabled={!isEdited} action={handleSave} />
          <button className="admin-user-action-button" onClick={handleUndoEdit}>
            <img
              width="25px"
              height="25px"
              src="/undo.svg"
              alt="Undo user edit button icon"
            />
          </button>
        </td>
      </tr>
    );
  }

  return (
    <tr key={form.id}>
      <td>{form.id}</td>
      <td>{form.firstname}</td>
      <td>{form.lastname}</td>
      <td>{form.phone}</td>
      <td>{form.address}</td>
      <td>{form.postcode}</td>
      <td>{form.email}</td>
      <td>
        <input
          type="checkbox"
          name="active"
          disabled
          checked={form.active}
          onChange={handleChange}
        />
      </td>
      <td>
        <select
          disabled={true}
          name="role"
          value={form.role}
          onChange={handleChange}
        >
          <option value="user">USER</option>
          <option value="admin">ADMIN</option>
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
