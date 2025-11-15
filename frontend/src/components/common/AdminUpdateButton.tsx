type AdminUpdateButtonProps = {
  action: () => void;
  disabled: boolean;
};

export default function AdminUpdateButton({
  action,
  disabled,
}: AdminUpdateButtonProps) {
  return (
    <button
      disabled={disabled}
      className={`admin-user-action-button' ${
        disabled && "admin-user-action-button--disabled"
      }`}
      onClick={action}
    >
      <img
        width="25px"
        height="25px"
        src="/save.svg"
        alt="Update user button icon"
      />
    </button>
  );
}
