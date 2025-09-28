import { Outlet } from "react-router-dom";
import Menu from "./components/nav/Menu";
import Footer from "./components/nav/Footer";

export default function Layout() {
  return (
    <>
      <Menu />
      <main className="main-div">
        <Outlet />
      </main>
      <Footer />
    </>
  );
}
