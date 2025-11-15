import { Link } from "react-router-dom";
import Searchbar from "./Searchbar";
import { useRef } from "react";
import { useAuthContext } from "../../context/AuthContext";

const NavBar = () => {
  // const { auth } = useAuthContext();
  const auth = true;
  // temporarily set auth to true

  const navbarButtonsRef = useRef<HTMLDivElement>(null);

  const toggleSidemenu = () => {
    navbarButtonsRef.current?.classList.toggle("navbar-buttons--active");
  };

  return (
    <nav className="navbar">
      <Link to="/" className="navbar-logo">
        <img width="50px" height="50px" src="/logo.png" alt="" />
        Bug'nBass
      </Link>
      <Searchbar />
      <div ref={navbarButtonsRef} className="navbar-buttons">
        <Link className="navbar-button" to="/listing">
          <img src="/guitar.svg" alt="" />
          <span>Produkte</span>
        </Link>
        <Link className="navbar-button" to="/contact">
          <img src="/contact.svg" alt="" />
          <span>Kontakt</span>
        </Link>
        <Link className="navbar-button" to="/cart">
          <img src="/cart.svg" alt="" />
          <span>Warenkorb</span>
        </Link>
        {auth ? (
          <Link className="navbar-button" to="/profile">
            <img src="/profile.svg" alt="" />
            <span>Profil</span>
          </Link>
        ) : (
          <Link className="navbar-button navbar-login-button" to="/auth/login">
            <span>Login</span>
          </Link>
        )}
      </div>
      <button onClick={toggleSidemenu} className="navbar-burger">
        <img src="/burger.svg" alt="" />
      </button>
    </nav>
  );
};

export default NavBar;
