import { Outlet } from "react-router-dom";
import Menu from "./layouts/Menu";
import Footer from "./layouts/Footer";

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
