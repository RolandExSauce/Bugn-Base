import { useLocation } from "react-router-dom";
import Login from "../pages/Login";
import Signup from "../pages/Register";

export default function Auth() {
  const location = useLocation();

  if (location.pathname === "/login") {
    return <Login />;
  } else if (location.pathname === "/signup") {
    return <Signup />;
  }

  return <div>Loading</div>;
}
