import { useState, useEffect } from "react";
import UserRow from "../../components/admin/User";
import type { User } from "../../types/models";
import { mockUser, mockUser2, mockUser3 } from "../../api/mock";


const UsersList = () => {
  const [users, setUsers] = useState<User[]>([]);

  const [selectedUserId, setSelectedUserId] = useState<string | null>(null);

  const handleSelect = (id: string) => {
    if (selectedUserId === id) return;
    setSelectedUserId(id);
  };

  // placeholder
  useEffect(() => {
    setUsers([mockUser, mockUser2, mockUser3]);
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
          <UserRow
            key={u.id}
            user={u}
            handleSelect={(id: string) => {
              handleSelect(id);
            }}
            selectedUserId={selectedUserId}
          />
        ))}
      </tbody>
    </table>
  );
};

export default UsersList;
