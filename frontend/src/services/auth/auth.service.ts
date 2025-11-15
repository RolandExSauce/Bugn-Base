import type { Login } from "../../types/models";

const BASE_URL = import.meta.env.VITE_BASE_URL;

export default class AuthService {
  public static login = async (loginForm: Login) => {
    const response = await fetch(`${BASE_URL}/auth/login`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(loginForm),
    });

    if (!response.ok) {
      throw new Error(`${response.status}`);
    }

    const data = await response.json();
    return data;
  };
}
