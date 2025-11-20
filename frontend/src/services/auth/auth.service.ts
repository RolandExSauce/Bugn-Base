import { jwtDecode } from "jwt-decode";
import type {
  AuthState,
  LoginDto,
  RegisterDto,
  Role,
} from "../../types/models";
import axios, { type AxiosInstance } from "axios";

interface TokenPayload {
  sub: string;
  role: string;
  exp: number;
  iat: number;
}

const BASE_URL = import.meta.env.VITE_BASE_URL;

class AuthService {
  private static api: AxiosInstance = axios.create({
    baseURL: `${BASE_URL}`,
    headers: {
      "Content-Type": "application/json",
    },
  });

  private static decodeRole(token: string) {
    const payload = jwtDecode<TokenPayload>(token);
    return payload.role;
  }

  // Attach token automatically to all requests
  static initializeInterceptors() {
    AuthService.api.interceptors.request.use(
      (config) => {
        const auth = localStorage.getItem("auth");
        if (auth) {
          const token = JSON.parse(auth).accessToken;
          if (config.headers) {
            config.headers.Authorization = `Bearer ${token}`;
          }
        }
        return config;
      },
      (error) => Promise.reject(error)
    );

    // Optional: response interceptor for handling 401 globally
    AuthService.api.interceptors.response.use(
      (response) => response,
      (error) => {
        if (error.response?.status === 401) {
          localStorage.removeItem("auth");
          // You could also trigger a global logout here if using context
          console.warn("Unauthorized, logging out...");
        }
        return Promise.reject(error);
      }
    );
  }

  public static login = async (
    loginForm: LoginDto,
    setAuth: React.Dispatch<React.SetStateAction<AuthState | null>>
  ) => {
    try {
      const response = await AuthService.api.post<AuthState>(
        "/auth/login",
        loginForm
      );
      const data = response.data;

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
      const response = await AuthService.api.post<AuthState>(
        "/auth/register",
        registerForm
      );
      const data = response.data;

      setAuth(data);
      localStorage.setItem("auth", JSON.stringify(data));
      return data;
    } catch (err: any) {
      throw new Error(err.response?.data?.message || "Registration failed");
    }
  };
}

// initialize interceptors once
AuthService.initializeInterceptors();

export default AuthService;
