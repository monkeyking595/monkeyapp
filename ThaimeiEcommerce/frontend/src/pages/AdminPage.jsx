import { useEffect, useState } from "react";
import { RefreshCw, Save } from "lucide-react";
import { api } from "../lib/api";
import { EmptyState, ErrorBanner, LoadingBlock } from "../components/StateBlocks";

const statusOptions = ["ACTIVE", "DEACTIVATED", "DELETED"];
const views = {
  customers: {
    label: "Customers",
    empty: "Customer accounts from the backend will appear here.",
    load: api.adminUsers
  },
  sellers: {
    label: "Sellers",
    empty: "Seller accounts from the backend will appear here.",
    load: api.adminSellers
  }
};

function label(value = "") {
  return value.replaceAll("_", " ");
}

export default function AdminPage() {
  const [users, setUsers] = useState([]);
  const [view, setView] = useState("customers");
  const [page, setPage] = useState(0);
  const [pageInfo, setPageInfo] = useState({ first: true, last: true, number: 0 });
  const [loading, setLoading] = useState(true);
  const [savingId, setSavingId] = useState(null);
  const [error, setError] = useState("");
  const [notice, setNotice] = useState("");

  useEffect(() => {
    loadUsers();
  }, [page, view]);

  async function loadUsers() {
    setError("");
    setLoading(true);

    try {
      const data = await views[view].load(page);
      setUsers(data.content);
      setPageInfo({
        first: data.first,
        last: data.last,
        number: data.number
      });
    } catch (err) {
      setError(err instanceof Error ? err.message : "Users could not load");
    } finally {
      setLoading(false);
    }
  }

  function changeView(nextView) {
    setNotice("");
    setError("");
    setView(nextView);
    setPage(0);
  }

  function changeStatus(userId, userStatus) {
    setUsers((current) =>
      current.map((user) => (user.id === userId ? { ...user, pendingStatus: userStatus } : user))
    );
  }

  async function saveStatus(user) {
    const nextStatus = user.pendingStatus || user.status || "ACTIVE";

    setError("");
    setNotice("");
    setSavingId(user.id);

    try {
      await api.updateUserStatus(user.id, nextStatus);
      setUsers((current) =>
        current.map((item) =>
          item.id === user.id ? { ...item, status: nextStatus, pendingStatus: undefined } : item
        )
      );
      setNotice(`${user.username || "User"} is now ${label(nextStatus).toLowerCase()}.`);
    } catch (err) {
      setError(err instanceof Error ? err.message : "User status could not be updated");
    } finally {
      setSavingId(null);
    }
  }

  return (
    <main className="page">
      <div className="page-heading">
        <div>
          <span className="pill">Admin</span>
          <h1>Users</h1>
        </div>
        <button className="icon-button" type="button" onClick={loadUsers} title="Refresh users">
          <RefreshCw size={18} />
        </button>
      </div>

      <div className="admin-toolbar" aria-label="Admin user type">
        {Object.entries(views).map(([key, item]) => (
          <button
            className={view === key ? "segment active" : "segment"}
            key={key}
            type="button"
            onClick={() => changeView(key)}
          >
            {item.label}
          </button>
        ))}
      </div>

      {error && <ErrorBanner message={error} />}
      {notice && <div className="banner success">{notice}</div>}
      {loading && <LoadingBlock label="Loading users" />}
      {!loading && !users.length && <EmptyState title="No users returned" text={views[view].empty} />}
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
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {users.map((user) => {
                const currentStatus = user.status || "ACTIVE";
                const selectedStatus = user.pendingStatus || currentStatus;
                const isDirty = selectedStatus !== currentStatus;
                const isSaving = savingId === user.id;

                return (
                  <tr key={user.id}>
                    <td>{user.id}</td>
                    <td>{user.username}</td>
                    <td>{user.email}</td>
                    <td>{user.role}</td>
                    <td>
                      <div className="status-control">
                        <span className={`status status-${selectedStatus.toLowerCase()}`}>{label(selectedStatus)}</span>
                        <select
                          value={selectedStatus}
                          onChange={(event) => changeStatus(user.id, event.target.value)}
                          disabled={isSaving}
                          aria-label={`Status for ${user.username || `user ${user.id}`}`}
                        >
                          {statusOptions.map((status) => (
                            <option key={status} value={status}>
                              {label(status)}
                            </option>
                          ))}
                        </select>
                      </div>
                    </td>
                    <td>
                      <button
                        className="icon-button"
                        type="button"
                        onClick={() => saveStatus(user)}
                        disabled={!isDirty || isSaving}
                        title="Save status"
                      >
                        <Save size={17} />
                      </button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      )}
      {!loading && !!users.length && (
        <div className="pager">
          <button className="button compact" type="button" onClick={() => setPage((value) => Math.max(0, value - 1))} disabled={pageInfo.first || loading}>
            Previous
          </button>
          <span>Page {pageInfo.number + 1}</span>
          <button className="button compact" type="button" onClick={() => setPage((value) => value + 1)} disabled={pageInfo.last || loading}>
            Next
          </button>
        </div>
      )}
    </main>
  );
}
