import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "bootstrap/dist/css/bootstrap.min.css";
import "./main.css";
import App from "./App.tsx";
import { BrowserRouter } from "react-router-dom";
import { CartContextProvider } from "./context/CartContext.tsx";
import { MyAuthProvider } from "./context/AuthContext.tsx";


createRoot(document.getElementById("root")!).render(
  <StrictMode>
    <MyAuthProvider>
      <CartContextProvider>
        <BrowserRouter>
          <App />
        </BrowserRouter>
      </CartContextProvider>
    </MyAuthProvider>
  </StrictMode>
);
