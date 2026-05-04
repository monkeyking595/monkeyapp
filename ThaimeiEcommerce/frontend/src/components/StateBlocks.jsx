export function EmptyState({ title, text }) {
  return (
    <div className="empty-state">
      <h2>{title}</h2>
      <p>{text}</p>
    </div>
  );
}

export function ErrorBanner({ message }) {
  return <div className="banner error">{message}</div>;
}

export function LoadingBlock({ label = "Loading" }) {
  return <div className="loading">{label}</div>;
}
