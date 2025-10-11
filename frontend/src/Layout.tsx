import { Outlet } from "react-router-dom";
import NavBar from "./components/navigation/NavBar";
import Footer from "./components/navigation/Footer";

export default function Layout() {
  return (
    <>
      <NavBar />
      <main className="main-div">
        <Outlet />
      </main>
      <Footer />
    </>
  );
}
