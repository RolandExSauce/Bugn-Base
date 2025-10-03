import { Link } from "react-router-dom";
import Searchbar from "./Searchbar";

const NavBar = () => {
  return (
    <nav className="navbar">
      <Link to="/" className="navbar-logo">
        <img width="50px" height="50px" src="/logo.png" alt="" />
        Bug'nBass
      </Link>
      <Searchbar />
      <div className="navbar-buttons">
        <Link className="navbar-button" to="/listing">
          <img src="/guitar.svg" alt="" />
          <span>Produkte</span>
        </Link>
        <Link className="navbar-button" to="/contact">
          <img src="/contact.svg" alt="" />
          <span>Kontakt</span>
        </Link>
        <Link className="navbar-button" to="/profile">
          <img src="/profile.svg" alt="" />
          <span>Profil</span>
        </Link>
        <Link className="navbar-button" to="/cart">
          <img src="/cart.svg" alt="" />
          <span>Warenkorb</span>
        </Link>
      </div>
    </nav>
  );
};

export default NavBar;
