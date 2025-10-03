const Login = () => {
  return (
    <main className="login-page">
      <form className="login-form">
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
          Noch kein Konto? <a href="/register">Jetzt registrieren</a>
        </p>
      </form>
    </main>
  );
};
export default Login;
