import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import type { Register } from "../../types/models";
import AuthService from "../../services/auth/auth.service";
import { useAuthContext } from "../../context/AuthContext";
import { EMAIL_REGEX, NAME_REGEX, PASSWORD_REGEX } from "../../types/regex";

const Register = () => {
  const navigate = useNavigate();

  const { setAuth } = useAuthContext();

  const [registerForm, setRegisterForm] = useState<Register>({
    firstname: "",
    lastname: "",
    email: "",
    password: "",
  });

  const [retypePassword, setRetypePassword] = useState<string>("");

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
  };

  const handleRegister = async (event: React.FormEvent) => {
    event.preventDefault();

    const newInvalidInput = {
      firstname: false,
      lastname: false,
      email: false,
      password: false,
      retypePassword: false,
    };

    let hasError = false;

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
      await AuthService.signup(registerForm, setAuth);
      navigate("/");
    } catch (error) {
      console.error(error);
      // todo: handle error
    }
  };

  return (
    <main className="login-page d-flex">
      <form
        onSubmit={handleRegister}
        className="login-form d-flex flex-column justify-content-center align-items-center gap-1"
      >
        <h1>Registrierung</h1>

        <label htmlFor="firstName">Vorname</label>
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
          <p className="text-danger">Vorname ist ungültig</p>
        )}

        <label htmlFor="lastName">Nachname</label>
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
          <p className="text-danger">Nachname ist ungültig</p>
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

        <button type="submit">Registrieren</button>

        <p className="register-hint">
          Bereits ein Konto? <Link to="/auth/login">Hier einloggen</Link>
        </p>
      </form>
    </main>
  );
};
export default Register;
