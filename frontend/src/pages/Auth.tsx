import { useLocation } from "react-router-dom";
import Login from "../components/auth/Login";
import Signup from "../components/auth/Signup";

export default function Auth() {
  const location = useLocation();

  if (location.pathname === "/login") {
    return <Login />;
  } else if (location.pathname === "/signup") {
    return <Signup />;
  }

  return <div>Loading</div>;
}
