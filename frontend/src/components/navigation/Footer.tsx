import { Link } from "react-router-dom";

export default function Footer() {
  return (
    <footer className="position-absolute bottom-0 start-0 d-flex w-100 justify-content-end bg-dark p-3">
      <Link to="/aboutus" className="footer-link">
        About us
      </Link>
    </footer>
  );
}
