import { createContext, useContext, useState, type ReactNode } from "react";
import type { AuthState, LoginDto, RegisterDto } from "../types/models";
import AuthService from "../services/auth/auth.service";

export interface AuthContextType {
  auth: AuthState | null;
  login: (loginForm: LoginDto) => Promise<void>;
  signup: (registerForm: RegisterDto) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const MyAuthProvider = ({ children }: { children: ReactNode }) => {
  const [auth, setAuth] = useState<AuthState | null>(() => {
    const savedAuth = localStorage.getItem("auth");
    return savedAuth ? JSON.parse(savedAuth) : null;
  });

  //auth service will handle data save
  const login = async (loginForm: LoginDto) => {
    await AuthService.login(loginForm, setAuth);
  };

  const signup = async (registerForm: RegisterDto) => {
    await AuthService.signup(registerForm, setAuth);
  };

  const logout = () => {
    AuthService.logout(setAuth);
  };

  return (
    <AuthContext.Provider value={{ auth, login, signup, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuthContext = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth must be used within AuthProvider");
  return context;
};
