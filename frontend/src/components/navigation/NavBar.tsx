import { Link } from "react-router-dom";
// import Searchbar from "./Searchbar";
import { useRef } from "react";
import { useCartContext } from "../../context/CartContext";
import { useAuthContext } from "../../context/AuthContext";

const NavBar = () => {
  const { auth } = useAuthContext();
  const { cart } = useCartContext();

  const navbarButtonsRef = useRef<HTMLDivElement>(null);

  const toggleSidemenu = () => {
    navbarButtonsRef.current?.classList.toggle("navbar-buttons--active");
  };

  const handleLinkClick = () => {
    navbarButtonsRef.current?.classList.remove("navbar-buttons--active");
  };

  return (
    <nav className="navbar">
      <Link to="/" className="navbar-logo">
        <img width="50px" height="50px" src="/logo.png" alt="" />
        Bug'nBass
      </Link>
      {/* <Searchbar /> */}
      <div ref={navbarButtonsRef} className="navbar-buttons">
        <Link className="navbar-button" to="/listing" onClick={handleLinkClick}>
          <img src="/guitar.svg" alt="Products icon" />
          <span>Produkte</span>
        </Link>
        <Link className="navbar-button" to="/contact" onClick={handleLinkClick}>
          <img src="/contact.svg" alt=" Contact icon" />
          <span>Kontakt</span>
        </Link>
        <Link className="navbar-button" to="/cart" onClick={handleLinkClick}>
          <img src="/cart.svg" alt=" Cart icon" />
          {cart && cart.length > 0 && (
            <span className="cart-amount-circle">
              {cart.reduce((a, b) => a + b.quantity, 0)}
            </span>
          )}
          <span>Warenkorb</span>
        </Link>
        {auth ? (
          <Link
            className="navbar-button"
            to="/profile"
            onClick={handleLinkClick}
          >
            <img src="/profile.svg" alt=" Profile icon" />
            <span>Profil</span>
          </Link>
        ) : (
          <Link
            className="navbar-button navbar-login-button"
            to="/auth/login"
            onClick={handleLinkClick}
          >
            <span>Login</span>
          </Link>
        )}
      </div>
      <button
        onClick={toggleSidemenu}
        className="navbar-burger"
        aria-label="Open burger menu"
      >
        <img src="/burger.svg" alt=" Burger icon" />
      </button>
    </nav>
  );
};

export default NavBar;
