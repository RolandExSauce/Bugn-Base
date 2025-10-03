const Register = () => {
  return (
    <main className="login-page">
      <form className="login-form">
        <h1>Registrierung</h1>

        <label htmlFor="firstName">Vorname</label>
        <input
          type="text"
          id="firstName"
          name="firstName"
          placeholder="Max"
          required
        />

        <label htmlFor="lastName">Nachname</label>
        <input
          type="text"
          id="lastName"
          name="lastName"
          placeholder="Mustermann"
          required
        />

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

        <label htmlFor="confirmPassword">Passwort wiederholen</label>
        <input
          type="password"
          id="confirmPassword"
          name="confirmPassword"
          placeholder="••••••••"
          required
        />

        <button type="submit">Registrieren</button>

        <p className="login-hint">
          Bereits ein Konto? <a href="/auth/login">Hier einloggen</a>
        </p>
      </form>
    </main>
  );
};
export default Register;
