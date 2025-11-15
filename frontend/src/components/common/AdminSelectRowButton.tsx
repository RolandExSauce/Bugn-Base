type AdminUpdateButtonProps = {
  action: () => void;
};

export default function AdminSelectRowButton({
  action,
}: AdminUpdateButtonProps) {
  return (
    <button className="admin-user-action-button" onClick={action}>
      <img
        width="25px"
        height="25px"
        src="/update.svg"
        alt="Update user button icon"
      />
    </button>
  );
}
