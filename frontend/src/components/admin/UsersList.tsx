// UsersList.tsx
import { useState, useEffect } from "react";
import UserRow from "./User";
import type { User } from "../../types/models";

const UsersList = () => {
  const [users, setUsers] = useState<User[]>([]);

  // placeholder
  useEffect(() => {
    setUsers([
      {
        id: "1",
        firstname: "Alice",
        lastname: "Smith",
        phone: "12345",
        address: "Street 1",
        postcode: 1000,
        email: "alice@mail.com",
        active: true,
        createdAt: new Date(),
        role: "USER",
      },
    ]);
  }, []);

  const handleUpdate = (updated: User) => {
    setUsers((prev) => prev.map((u) => (u.id === updated.id ? updated : u)));
    // later send PUT request to backend here
  };

  return (
    <table className="table table-striped">
      <thead>
        <tr>
          <th>ID</th>
          <th>Firstname</th>
          <th>Lastname</th>
          <th>Phone</th>
          <th>Address</th>
          <th>Postcode</th>
          <th>Email</th>
          <th>Active</th>
          <th>Role</th>
          <th>Action</th>
        </tr>
      </thead>
      <tbody>
        {users.map((u) => (
          <UserRow key={u.id} user={u} onUpdate={handleUpdate} />
        ))}
      </tbody>
    </table>
  );
};

export default UsersList;
