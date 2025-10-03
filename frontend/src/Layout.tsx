import { Outlet } from "react-router-dom";
import NavBar from "./components/navigation/NavBar";

export default function Layout() {
  return (
    <>
      <NavBar />
      <main className="main-div">
        <Outlet />
      </main>
    </>
  );
}
