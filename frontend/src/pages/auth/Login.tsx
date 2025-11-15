import { useState } from "react";
import AuthService from "../../services/auth/auth.service";
import { Link } from "react-router-dom";
import type { Login } from "../../types/models";

const Login = () => {
  const [loginForm, setLoginForm] = useState<Login>({
    email: "",
    password: "",
  });

  const [invalidInput, setInvalidInput] = useState({
    email: false,
    password: false,
  });

  const handlePasswordInvalid = () => {
    setInvalidInput({
      ...invalidInput,
      password: true,
    });
  };

  const handleEmailInvalid = () => {
    setInvalidInput({
      ...invalidInput,
      email: true,
    });
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setLoginForm({
      ...loginForm,
      [e.target.name]: e.target.value,
    });
  };

  const handleLogin = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    try {
      const data = await AuthService.login(loginForm);
    } catch (error) {
      // todo: handle error
    } finally {
      // reset invalid messages
      setInvalidInput({
        email: false,
        password: false,
      });
    }
  };

  return (
    <main className="login-page d-flex">
      <form className="login-form d-flex flex-column justify-content-center align-items-center gap-3">
        <h1>Anmeldung</h1>

        <label htmlFor="email">E-Mail</label>
        <input
          type="email"
          id="email"
          name="email"
          placeholder="name@example.com"
          pattern="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$"
          onInvalid={handleEmailInvalid}
          onChange={handleChange}
          value={loginForm.email}
          required
        />

        {invalidInput.email && (
          <p className="text-danger">Please enter a valid email address.</p>
        )}

        <label htmlFor="password">Passwort</label>
        <input
          type="password"
          id="password"
          name="password"
          placeholder="••••••••"
          // at least 1 upper, 1 lower case and 1 number (min 8 characters)
          pattern="^(?=.*[A-Z])(?=.*[a-z])(?=.*\d).{8,}$"
          onInvalid={handlePasswordInvalid}
          onChange={handleChange}
          value={loginForm.password}
          required
        />

        {invalidInput.password && (
          <p className="text-danger">
            The password must be at least 8 characters long and contain at least
            one uppercase letter, one lowercase letter, and one number.
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
