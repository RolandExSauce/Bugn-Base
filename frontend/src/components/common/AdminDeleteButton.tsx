type AdminDeleteButtonProps = {
  action: () => void;
};

export default function AdminDeleteButton({ action }: AdminDeleteButtonProps) {
  return (
    <button className="admin-user-action-button" onClick={action}>
      <img
        width="25px"
        height="25px"
        src="/delete.svg"
        alt="Delete user button icon"
      />
    </button>
  );
}
