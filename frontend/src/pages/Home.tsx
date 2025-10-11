import { Link } from "react-router-dom";
import ShopItem from "../components/product/ShopItem";

export default function HomePage() {
  return (
    <div className="homepage-main d-flex flex-column align-items-center text-center gap-4 p-4">
      <h1>Willkommen!</h1>
      <h2>Entdecken Sie hochwertige Instrumente</h2>

      <Link to="/listing" className="btn btn-primary">
        Jetzt entdecken
      </Link>

      <div className="d-flex flex-wrap justify-content-center gap-4 mt-4">
        <ShopItem />
        <ShopItem />
        <ShopItem />
      </div>
    </div>
  );
}
