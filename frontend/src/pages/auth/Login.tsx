import { useState } from "react";
import AuthService from "../../services/auth/auth.service";
import { Link, useNavigate } from "react-router-dom";
import type { Login } from "../../types/models";
import { useAuthContext } from "../../context/AuthContext";

const Login = () => {
  const navigate = useNavigate();
  const { setAuth } = useAuthContext();

  const [loginForm, setLoginForm] = useState<Login>({
    email: "",
    password: "",
  });

  const [invalidInput, setInvalidInput] = useState({
    email: false,
    password: false,
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setLoginForm({
      ...loginForm,
      [e.target.name]: e.target.value,
    });
  };

  const handleLogin = async (event: React.FormEvent) => {
    event.preventDefault();

    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    const passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d).{8,}$/;

    let hasError = false;

    const newInvalidInput = { email: false, password: false };

    if (!emailRegex.test(loginForm.email)) {
      newInvalidInput.email = true;
      hasError = true;
    }

    if (!passwordRegex.test(loginForm.password)) {
      newInvalidInput.password = true;
      hasError = true;
    }

    setInvalidInput(newInvalidInput);

    if (hasError) return;

    try {
      await AuthService.login(loginForm, setAuth);
      navigate("/");
    } catch (error) {
      console.error(error);
      // todo: handle error
    }
  };

  return (
    <main className="login-page d-flex">
      <form
        onSubmit={handleLogin}
        className="login-form d-flex flex-column justify-content-center align-items-center gap-3"
      >
        <h1>Anmeldung</h1>

        <label htmlFor="email">E-Mail</label>
        <input
          type="text"
          id="email"
          name="email"
          placeholder="name@example.com"
          onChange={handleChange}
          value={loginForm.email}
          required
        />

        {invalidInput.email && (
          <p className="text-danger">E-Mail ist ungültig</p>
        )}

        <label htmlFor="password">Passwort</label>
        <input
          type="password"
          id="password"
          name="password"
          placeholder="••••••••"
          onChange={handleChange}
          value={loginForm.password}
          required
        />

        {invalidInput.password && (
          <p className="text-danger">
            Passwort muss mindestens 8 Zeichen haben, eine Groß- und eine
            Kleinbuchstabe und eine Zahl enthalten
          </p>
        )}

        <button type="submit">Einloggen</button>

        <p className="register-hint">
          Noch kein Konto? <Link to="/auth/signup">Jetzt registrieren</Link>
        </p>
      </form>
    </main>
  );
};
export default Login;
