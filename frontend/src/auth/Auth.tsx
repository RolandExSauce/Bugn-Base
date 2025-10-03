import { useParams } from "react-router-dom";
import Login from "../pages/Login";
import Signup from "../pages/Register";

export default function Auth() {
  const { action } = useParams();

  console.log(action);

  if (action === "login") {
    return <Login />;
  } else if (action === "signup") {
    return <Signup />;
  }

  return <div>Loading</div>;
}
