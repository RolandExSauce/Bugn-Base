import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import type { RegisterDto } from "../../types/models";
import { EMAIL_REGEX, NAME_REGEX, PASSWORD_REGEX } from "../../utils/regex";
import { useAuthContext } from "../../context/AuthContext";

const Register = () => {
  const navigate = useNavigate();
  const { signup } = useAuthContext();

  const [registerForm, setRegisterForm] = useState<RegisterDto>({
    firstname: "",
    lastname: "",
    email: "",
    password: "",
  });

  const [retypePassword, setRetypePassword] = useState<string>("");
  const [registrationError, setRegistrationError] = useState<string | null>(
    null,
  );

  const [invalidInput, setInvalidInput] = useState({
    firstname: false,
    lastname: false,
    email: false,
    password: false,
    retypePassword: false,
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    setRegisterForm({
      ...registerForm,
      [e.target.name]: e.target.value,
    });
    // Clear error when user types
    setRegistrationError(null);
  };

  const handleRegister = async (event: React.FormEvent) => {
    event.preventDefault();
    setRegistrationError(null);

    const newInvalidInput = {
      firstname: false,
      lastname: false,
      email: false,
      password: false,
      retypePassword: false,
    };

    let hasError = false;

    // Validate with regex
    if (!NAME_REGEX.test(registerForm.firstname)) {
      newInvalidInput.firstname = true;
      hasError = true;
    }

    if (!NAME_REGEX.test(registerForm.lastname)) {
      newInvalidInput.lastname = true;
      hasError = true;
    }

    if (!EMAIL_REGEX.test(registerForm.email)) {
      newInvalidInput.email = true;
      hasError = true;
    }

    if (!PASSWORD_REGEX.test(registerForm.password)) {
      newInvalidInput.password = true;
      hasError = true;
    }

    if (registerForm.password !== retypePassword) {
      newInvalidInput.retypePassword = true;
      hasError = true;
    }

    setInvalidInput(newInvalidInput);

    if (hasError) return;

    try {
      await signup(registerForm);
      navigate("/");
    } catch (error: any) {
      console.error("Registration error:", error);
      setRegistrationError(error.message || "Registrierung fehlgeschlagen");
    }
  };

  return (
    <main className="login-page d-flex">
      <form
        onSubmit={handleRegister}
        className="login-form d-flex flex-column justify-content-center align-items-center gap-1"
      >
        <h1>Registrierung</h1>

        {/* Display registration error */}
        {registrationError && (
          <div className="alert alert-danger w-100 text-center" role="alert">
            {registrationError}
          </div>
        )}

        <label htmlFor="firstname">Vorname</label>
        <input
          type="text"
          id="firstname"
          name="firstname"
          placeholder="Max"
          onChange={handleChange}
          value={registerForm.firstname}
          required
        />

        {invalidInput.firstname && (
          <p className="text-danger">
            Vorname ist ungültig (nur Buchstaben, 1-50 Zeichen)
          </p>
        )}

        <label htmlFor="lastname">Nachname</label>
        <input
          type="text"
          id="lastname"
          name="lastname"
          onChange={handleChange}
          value={registerForm.lastname}
          placeholder="Mustermann"
          required
        />

        {invalidInput.lastname && (
          <p className="text-danger">
            Nachname ist ungültig (nur Buchstaben, 1-50 Zeichen)
          </p>
        )}

        <label htmlFor="email">E-Mail</label>
        <input
          type="text"
          id="email"
          name="email"
          value={registerForm.email}
          placeholder="name@example.com"
          onChange={handleChange}
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
          value={registerForm.password}
          onChange={handleChange}
          required
        />

        {invalidInput.password && (
          <p className="text-danger">
            Passwort muss mindestens 8 Zeichen haben, eine Groß- und eine
            Kleinbuchstabe und eine Zahl enthalten
          </p>
        )}

        <label htmlFor="confirmPassword">Passwort wiederholen</label>
        <input
          type="password"
          id="confirmPassword"
          name="confirmPassword"
          placeholder="••••••••"
          value={retypePassword}
          onChange={(e) => setRetypePassword(e.target.value)}
          required
        />

        {invalidInput.retypePassword && (
          <p className="text-danger">Passwörter stimmen nicht überein</p>
        )}

        <button aria-label="Registrieren" type="submit">
          Registrieren
        </button>

        <p className="register-hint">
          Bereits ein Konto? <Link to="/auth/login">Hier einloggen</Link>
        </p>
      </form>
    </main>
  );
};

export default Register;
