import { Link } from "react-router-dom";

const Login = () => {
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
          required
        />

        <label htmlFor="password">Passwort</label>
        <input
          type="password"
          id="password"
          name="password"
          placeholder="••••••••"
          required
        />

        <button type="submit">Einloggen</button>

        <p className="register-hint">
          Noch kein Konto? <Link to="/auth/signup">Jetzt registrieren</Link>
        </p>
      </form>
    </main>
  );
};
export default Login;
