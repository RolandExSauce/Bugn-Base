import type { AuthState, LoginDto, RegisterDto } from "../../types/models";

const BASE_URL = import.meta.env.VITE_BASE_URL;

export default class AuthService {
  public static login = async (
    loginForm: LoginDto,
    setAuth: React.Dispatch<React.SetStateAction<AuthState | undefined>>
  ) => {
    console.log(BASE_URL);
    const response = await fetch(`${BASE_URL}/bugnbass/auth/login`, {
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
    // assuming BE returns a response "AuthState", sets the auth context
    setAuth(data);
    // stores auth data in local storage
    localStorage.setItem("auth", JSON.stringify(data));
    return data;
  };

  public static logout = (
    setAuth: React.Dispatch<React.SetStateAction<AuthState | undefined>>
  ) => {
    // removes auth data from local storage
    localStorage.removeItem("auth");
    // sets the auth context to undefined
    setAuth(undefined);
  };

  public static signup = async (
    registerForm: RegisterDto,
    setAuth: React.Dispatch<React.SetStateAction<AuthState | undefined>>
  ) => {
    const response = await fetch(`${BASE_URL}/bugnbass/auth/register`, {
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

    // assuming backend automatically logs the user in after registering. returning a  response "AuthState"
    try {
      const data = await response.json();
      setAuth(data);
      return data;
    } catch {
      return;
      // auto login upon signup failed. doesnt have to be handled necessarily
    }
  };
}
