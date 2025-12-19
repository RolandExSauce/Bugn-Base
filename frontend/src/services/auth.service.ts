import { apiClient } from "../api/api-client";
import type { AuthState, LoginDto, RegisterDto, Role } from "../types/models";
import { jwtDecode } from "jwt-decode";

interface TokenPayload {
  sub: string;
  role: string;
  exp: number;
  iat: number;
}

class AuthService {
  private static decodeRole(token: string) {
    const payload = jwtDecode<TokenPayload>(token);
    return payload.role;
  }

  public static login = async (
    loginForm: LoginDto,
    setAuth: React.Dispatch<React.SetStateAction<AuthState | null>>
  ) => {
    try {
      const data = await apiClient.post<AuthState>("/auth/login", loginForm);
      const role = AuthService.decodeRole(data.accessToken) as Role;

      const newAuthState: AuthState = {
        user: data.user,
        accessToken: data.accessToken,
        role,
      };

      setAuth(newAuthState);
      localStorage.setItem("auth", JSON.stringify(newAuthState));
      return data;
    } catch (err: any) {
      throw new Error(err.response?.data?.message || "Login failed");
    }
  };

  public static logout = (
    setAuth: React.Dispatch<React.SetStateAction<AuthState | null>>
  ) => {
    localStorage.removeItem("auth");
    setAuth(null);
    window.location.href = "/";
  };

  public static signup = async (
    registerForm: RegisterDto,
    setAuth: React.Dispatch<React.SetStateAction<AuthState | null>>
  ) => {
    try {
      const data = await apiClient.post<AuthState>(
        "/auth/register",
        registerForm
      );

      const role = AuthService.decodeRole(data.accessToken) as Role;
      const newAuthState: AuthState = {
        user: data.user,
        accessToken: data.accessToken,
        role,
      };

      setAuth(newAuthState);
      localStorage.setItem("auth", JSON.stringify(newAuthState));
      return data;
    } catch (err: any) {
      throw new Error(err.response?.data?.message || "Registration failed");
    }
  };
}

export default AuthService;
