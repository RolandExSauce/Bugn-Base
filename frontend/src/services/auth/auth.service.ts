import type { AuthState, Login, Register } from "../../types/models";

const BASE_URL = import.meta.env.VITE_BASE_URL;

export default class AuthService {
  public static login = async (
    loginForm: Login,
    setAuth: React.Dispatch<React.SetStateAction<AuthState | undefined>>
  ) => {
    const response = await fetch(`${BASE_URL}/auth/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(loginForm),
    });

    if (!response.ok) {
      throw new Error(`${response.status}`);
      //todo: catch this in the component and show an error message
    }

    const data = await response.json();
    setAuth(data);
    return data;
  };

  public static signup = async (
    registerForm: Register,
    setAuth: React.Dispatch<React.SetStateAction<AuthState | undefined>>
  ) => {
    const response = await fetch(`${BASE_URL}/auth/signup`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(registerForm),
    });

    if (!response.ok) {
      throw new Error(`${response.status}`);
      //todo: catch this in the component and show an error message
    }

    // assuming backend logs the user in after registering. returning a login response
    const data = await response.json();
    setAuth(data);
    return data;
  };
}
