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
import Product from "./pages/Product";
import ProtectedRoute from "./ProtectedRoute";

export default function App() {
  return (
    <>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route index element={<Homepage />} />
          <Route path="/aboutus" element={<AboutUs />} />
          <Route
            path="/profile"
            element={
              <ProtectedRoute>
                <Profile />
              </ProtectedRoute>
            }
          />
          <Route
            path="/admin"
            element={
              <ProtectedRoute>
                <AdminPanel />
              </ProtectedRoute>
            }
          />
          <Route path="/auth/:action" element={<Auth />} />
          <Route path="/cart" element={<Cart />} />
          <Route path="/cart/checkout" element={<Checkout />} />
          <Route path="/contact" element={<Contact />} />
          <Route path="/listing" element={<Listing />} />
          <Route path="/product/:productId" element={<Product />} />
        </Route>
      </Routes>
    </>
  );
}
