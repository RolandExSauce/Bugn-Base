import { Link } from "react-router-dom";
import Searchbar from "./Searchbar";

const NavBar = () => {
  return (
    <nav className="navbar">
      <div className="navbar-logo">
        <img width="50px" height="50px" src="/logo.png" alt="" />
        Bug'nBass
      </div>
      <Searchbar />
      <div className="navbar-buttons">
        <Link to="/login">
          <button className="navbar-button">Produkte</button>
        </Link>
        <Link to="/login">
          <button className="navbar-button">Kontakt</button>
        </Link>
        <Link to="/login">
          <button className="navbar-button">Profil</button>
        </Link>
        <Link to="/login">
          <button className="navbar-button">Warenkorb</button>
        </Link>
      </div>
    </nav>
  );
};

export default NavBar;
