import { useState } from "react";
import { ShieldPlus } from "lucide-react";
import { api } from "../lib/api";
import { ErrorBanner } from "../components/StateBlocks";

const initialForm = {
  adminname: "",
  adminemail: "",
  adminpassword: "",
  adminconfirmpassword: ""
};

export default function AdminRegistrationPage() {
  const [form, setForm] = useState(initialForm);
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState("");
  const [notice, setNotice] = useState("");

  function update(key, value) {
    setForm((current) => ({ ...current, [key]: value }));
  }

  async function submit(event) {
    event.preventDefault();
    setError("");
    setNotice("");
    setBusy(true);

    try {
      await api.adminRegister(form.adminname, form.adminemail, form.adminpassword, form.adminconfirmpassword);
      setForm(initialForm);
      setNotice("Admin account created.");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Admin account could not be created");
    } finally {
      setBusy(false);
    }
  }

  return (
    <main className="page narrow">
      <div className="page-heading">
        <div>
          <span className="pill">Admin</span>
          <h1>Register admin</h1>
        </div>
        <ShieldPlus size={30} />
      </div>

      {error && <ErrorBanner message={error} />}
      {notice && <div className="banner success">{notice}</div>}

      <form className="profile-form" onSubmit={submit}>
        <label>
          Username
          <input
            value={form.adminname}
            onChange={(event) => update("adminname", event.target.value)}
            minLength={3}
            maxLength={20}
            required
          />
        </label>
        <label>
          Email
          <input
            type="email"
            value={form.adminemail}
            onChange={(event) => update("adminemail", event.target.value)}
            maxLength={50}
            required
          />
        </label>
        <label>
          Password
          <input
            type="password"
            value={form.adminpassword}
            onChange={(event) => update("adminpassword", event.target.value)}
            minLength={8}
            maxLength={20}
            required
          />
        </label>
        <label>
          Confirm password
          <input
            type="password"
            value={form.adminconfirmpassword}
            onChange={(event) => update("adminconfirmpassword", event.target.value)}
            minLength={8}
            maxLength={20}
            required
          />
        </label>
        <button className="button span-two" disabled={busy} type="submit">
          <ShieldPlus size={18} />
          {busy ? "Creating..." : "Create admin"}
        </button>
      </form>
    </main>
  );
}
