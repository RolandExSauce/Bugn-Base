// UsersList.tsx
import { useState, useEffect } from "react";
import UserRow from "../../components/admin/User";
import type { User } from "../../types/models";
import { mockUser } from "../../types/temp/PlaceholderData";

const UsersList = () => {
  const [users, setUsers] = useState<User[]>([]);

  // placeholder
  useEffect(() => {
    setUsers([
      mockUser,
      mockUser,
      mockUser,
      mockUser,
      mockUser,
      mockUser,
      mockUser,
    ]);
  }, []);

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
          <UserRow key={u.id} user={u} />
        ))}
      </tbody>
    </table>
  );
};

export default UsersList;
