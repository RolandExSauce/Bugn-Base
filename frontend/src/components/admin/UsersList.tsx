import { useEffect, useState } from "react";
import type { User } from "../../types/models";
import AdminUserService from "../../services/admin.user.service";
import UserRow from "./User";

export default function UsersList() {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedUserId, setSelectedUserId] = useState<string | null>(null);

  const fetchUsers = async () => {
    try {
      setLoading(true);
      const data = await AdminUserService.getAllUsers();
      setUsers(data);
      setError(null);
    } catch (err) {
      setError("Benutzer konnten nicht geladen werden.");
      console.error("Error fetching users:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  const handleSelect = (id: string) => {
    setSelectedUserId(id || null);
  };

  const handleUserUpdated = (updatedUser: User) => {
    setUsers((prev) =>
      prev.map((u) => (u.id === updatedUser.id ? updatedUser : u)),
    );
    setSelectedUserId(null);
  };

  const handleUserDeleted = (deletedUserId: string) => {
    setUsers((prev) => prev.filter((u) => u.id !== deletedUserId));
    setSelectedUserId(null);
  };

  if (loading) {
    return (
      <div className="d-flex justify-content-center align-items-center py-5">
        <div className="spinner-border text-primary" role="status" />
        <p className="mt-3 ms-3">Lade Benutzer...</p>
      </div>
    );
  }

  if (error) {
    return (
      <div className="alert alert-danger">
        {error}
        <button
          onClick={fetchUsers}
          className="btn btn-sm btn-outline-danger ms-3"
          aria-label="Erneut versuchen"
        >
          Erneut versuchen
        </button>
      </div>
    );
  }

  if (users.length === 0) {
    return <div className="alert alert-info">Keine Benutzer vorhanden.</div>;
  }

  return (
    <div className="table-responsive">
      <table className="table table-bordered table-striped align-middle">
        <thead>
          <tr>
            <th>ID</th>
            <th>Vorname</th>
            <th>Nachname</th>
            <th>Telefon</th>
            <th>Adresse</th>
            <th>PLZ</th>
            <th>E-Mail</th>
            <th>Aktiv</th>
            <th>Rolle</th>
            <th>Aktionen</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => (
            <UserRow
              key={user.id}
              user={user}
              selectedUserId={selectedUserId}
              handleSelect={handleSelect}
              onUpdated={handleUserUpdated}
              onDeleted={handleUserDeleted}
            />
          ))}
        </tbody>
      </table>
    </div>
  );
}
