import { useRef, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import type { LoginDto } from "../../types/models";
import { EMAIL_REGEX } from "../../utils/regex";
import { useAuthContext } from "../../context/AuthContext";

const Login = () => {
  const navigate = useNavigate();
  const { login } = useAuthContext();

  const mainRef = useRef<HTMLFormElement>(null);

  const [loginForm, setLoginForm] = useState<LoginDto>({
    email: "",
    password: "",
  });

  const [invalidInput, setInvalidInput] = useState({
    email: false,
    password: false,
  });

  // Add state for login error
  const [loginError, setLoginError] = useState<string | null>(null);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setLoginForm({
      ...loginForm,
      [e.target.name]: e.target.value,
    });
    // Clear error when user starts typing
    setLoginError(null);
  };

  const handleLogin = async (event: React.FormEvent) => {
    event.preventDefault();
    setLoginError(null); // Clear any previous errors

    let hasError = false;

    const newInvalidInput = { email: false, password: false };

    if (!EMAIL_REGEX.test(loginForm.email)) {
      newInvalidInput.email = true;
      hasError = true;
    }

    setInvalidInput(newInvalidInput);

    if (hasError) return;

    try {
      await login(loginForm);

      // success background color animation
      mainRef.current?.classList.remove("success-animation");
      void mainRef.current?.offsetWidth;
      mainRef.current?.classList.add("success-animation");

      setTimeout(() => {
        navigate("/");
      }, 800);
    } catch (error: any) {
      console.error(error);
      // Set the error message to display in UI
      setLoginError("Invalid Credentials");
    }
  };

  return (
    <main className="login-page d-flex">
      <form
        ref={mainRef}
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

        {/* Display login error message */}
        {loginError && (
          <div className="alert alert-danger w-100 text-center" role="alert">
            {loginError}
          </div>
        )}

        <button type="submit" aria-label="Einloggen">
          Einloggen
        </button>

        <p className="register-hint">
          Noch kein Konto? <Link to="/auth/signup">Jetzt registrieren</Link>
        </p>
      </form>
    </main>
  );
};
export default Login;
