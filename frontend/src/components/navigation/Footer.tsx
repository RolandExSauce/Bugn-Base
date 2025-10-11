import { Link } from "react-router-dom";

export default function Footer() {
  return (
    <footer className="footer d-flex w-100 justify-content-end align-items-center bg-dark p-3">
      <Link to="/aboutus">About us</Link>
    </footer>
  );
}
