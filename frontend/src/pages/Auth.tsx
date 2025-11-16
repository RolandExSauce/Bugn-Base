import { useParams } from "react-router-dom";
import Signup from "./auth/Register";
import Login from "./auth/Login";

export default function Auth() {
  const { action } = useParams();

  return (
    <div className="auth-page">
      {action === "login" && <Login />}
      {action === "signup" && <Signup />}
    </div>
  );
}
