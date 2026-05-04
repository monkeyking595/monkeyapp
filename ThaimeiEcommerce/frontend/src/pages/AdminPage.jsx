import { useEffect, useState } from "react";
import { UsersRound } from "lucide-react";
import { api } from "../lib/api";
import { EmptyState, ErrorBanner, LoadingBlock } from "../components/StateBlocks";

export default function AdminPage() {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    api
      .adminUsers()
      .then(setUsers)
      .catch((err) => setError(err instanceof Error ? err.message : "Users could not load"))
      .finally(() => setLoading(false));
  }, []);

  return (
    <main className="page">
      <div className="page-heading">
        <div>
          <span className="pill">Admin</span>
          <h1>Users</h1>
        </div>
        <UsersRound size={30} />
      </div>
      {error && <ErrorBanner message={error} />}
      {loading && <LoadingBlock label="Loading users" />}
      {!loading && !users.length && <EmptyState title="No users returned" text="Admin users from the backend will appear here." />}
      {!!users.length && (
        <div className="table-wrap">
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Username</th>
                <th>Email</th>
                <th>Role</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {users.map((user) => (
                <tr key={user.id}>
                  <td>{user.id}</td>
                  <td>{user.username}</td>
                  <td>{user.email}</td>
                  <td>{user.role}</td>
                  <td>
                    <span className="status">{user.status || "Active"}</span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </main>
  );
}
