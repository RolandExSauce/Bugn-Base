import { useParams } from "react-router-dom";
import Login from "../auth/Login";
import Signup from "../auth/Register";

export default function Auth() {
  const { action } = useParams();

  return (
    <div className="auth-page">
      {action === "login" && <Login />}
      {action === "signup" && <Signup />}
    </div>
  );
}
