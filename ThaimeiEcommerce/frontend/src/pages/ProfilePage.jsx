import { useEffect, useState } from "react";
import { Save } from "lucide-react";
import { api } from "../lib/api";
import { ErrorBanner, LoadingBlock } from "../components/StateBlocks";

const emptyProfile = {
  fullname: "",
  email: "",
  phone: "",
  age: 18,
  gender: "",
  country: "",
  city: "",
  state: "",
  zip: "",
  locality: ""
};

export default function ProfilePage() {
  const [profile, setProfile] = useState(emptyProfile);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [notice, setNotice] = useState("");

  useEffect(() => {
    api
      .profile()
      .then(setProfile)
      .catch(() => setProfile(emptyProfile))
      .finally(() => setLoading(false));
  }, []);

  async function submit(event) {
    event.preventDefault();
    setError("");
    setNotice("");
    try {
      await api.saveProfile(profile);
      setNotice("Profile saved.");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Profile could not be saved");
    }
  }

  function update(key, value) {
    setProfile((current) => ({ ...current, [key]: value }));
  }

  return (
    <main className="page narrow">
      <div className="page-heading">
        <div>
          <span className="pill">Account</span>
          <h1>Profile</h1>
        </div>
      </div>
      {error && <ErrorBanner message={error} />}
      {notice && <div className="banner success">{notice}</div>}
      {loading ? (
        <LoadingBlock label="Loading profile" />
      ) : (
        <form className="profile-form" onSubmit={submit}>
          <label>
            Full name
            <input value={profile.fullname} onChange={(event) => update("fullname", event.target.value)} required />
          </label>
          <label>
            Email
            <input type="email" value={profile.email} onChange={(event) => update("email", event.target.value)} required />
          </label>
          <label>
            Phone
            <input value={profile.phone} onChange={(event) => update("phone", event.target.value)} pattern="[0-9]{10}" required />
          </label>
          <label>
            Age
            <input
              type="number"
              min={1}
              max={120}
              value={profile.age}
              onChange={(event) => update("age", Number(event.target.value))}
              required
            />
          </label>
          <label>
            Gender
            <select value={profile.gender} onChange={(event) => update("gender", event.target.value)} required>
              <option value="">Select</option>
              <option>Female</option>
              <option>Male</option>
              <option>Non-binary</option>
              <option>Prefer not to say</option>
            </select>
          </label>
          <label>
            Country
            <input value={profile.country} onChange={(event) => update("country", event.target.value)} required />
          </label>
          <label>
            State
            <input value={profile.state} onChange={(event) => update("state", event.target.value)} required />
          </label>
          <label>
            City
            <input value={profile.city} onChange={(event) => update("city", event.target.value)} required />
          </label>
          <label>
            ZIP
            <input value={profile.zip} onChange={(event) => update("zip", event.target.value)} required />
          </label>
          <label className="span-two">
            Locality
            <input value={profile.locality} onChange={(event) => update("locality", event.target.value)} required />
          </label>
          <button className="button span-two" type="submit">
            <Save size={18} />
            Save profile
          </button>
        </form>
      )}
    </main>
  );
}
