import { Route, Routes } from "react-router-dom";
import Layout from "./Layout";
import Homepage from "./pages/Home";
import Profile from "./pages/user/UserProfil";
import AboutUs from "./pages/About";
import AdminPanel from "./pages/admin/AdminPanel";
import Auth from "./pages/Auth";
import Cart from "./components/cart/Cart";
import Checkout from "./pages/Checkout";
import Contact from "./pages/Contact";
import Listing from "./pages/Listing";
import Orders from "./pages/Orders";
import Product from "./pages/Product";

export default function App() {
  return (
    <>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<Homepage />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/aboutus" element={<AboutUs />} />
          <Route path="/admin" element={<AdminPanel />} />
          <Route path="/auth/:action" element={<Auth />} />
          <Route path="/cart" element={<Cart />} />
          <Route path="/checkout" element={<Checkout />} />
          <Route path="/contact" element={<Contact />} />
          <Route path="/listing" element={<Listing />} />
          <Route path="/orders" element={<Orders />} />
          <Route path="/product/:productId" element={<Product />} />
        </Route>
      </Routes>
    </>
  );
}
