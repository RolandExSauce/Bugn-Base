import { useState, useEffect } from "react";
import type { User } from "../../types/models";
import AdminService from "../../services/admin/admin.service";

interface UserProps {
  user: User;
}

const UserRow = ({ user }: UserProps) => {
  const [form, setForm] = useState<User>(user);

  useEffect(() => setForm(user), [user]);

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value } = e.target;

    if (e.target instanceof HTMLInputElement && e.target.type === "checkbox") {
      setForm({ ...form, [name]: e.target.checked });
    } else {
      setForm({ ...form, [name]: value });
    }
  };
  const handleSave = () => {
    AdminService.updateUser(form);
  };

  const handleDelete = () => {
    AdminService.deleteUser(form.id);
  };

  return (
    <tr>
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
          checked={form.active}
          onChange={handleChange}
        />
      </td>
      <td>
        <select name="role" value={form.role} onChange={handleChange}>
          <option value="USER">USER</option>
          <option value="ADMIN">ADMIN</option>
        </select>
      </td>
      <td>
        <button onClick={handleSave}>Update</button>
        <button onClick={handleDelete}>Delete</button>
      </td>
    </tr>
  );
};

export default UserRow;
