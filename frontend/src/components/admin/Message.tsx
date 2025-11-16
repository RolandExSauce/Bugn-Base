import type { MessageDto } from "../../types/models";
import AdminDeleteButton from "../common/AdminDeleteButton";

export default function Message({ message }: { message: MessageDto }) {
  return (
    <div className="card mb-3">
      <div className="card-body d-flex flex-column gap-2">
        <div className="d-flex justify-content-between">
          <span className="fw-bold">{message.name}</span>
          <small className="text-muted">
            {message.createdAt.toLocaleString()}
          </small>
        </div>
        <span className="text-muted">{message.email}</span>
        <h6 className="mt-1">{message.subject}</h6>
        <p className="mb-0">{message.message}</p>
      </div>
      <div className="text-end m-1">
        <AdminDeleteButton action={() => {}} />
      </div>
    </div>
  );
}
