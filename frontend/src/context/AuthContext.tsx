import { createContext, useContext, useState } from "react";
import type { AuthState, LoginDto, RegisterDto } from "../types/models";
import AuthService from "../services/auth/auth.service";

interface AuthContextType {
  auth: AuthState | null;
  login: (data: LoginDto) => Promise<void>;
  signup: (data: RegisterDto) => Promise<void>;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

// eslint-disable-next-line react-refresh/only-export-components
export const useAuthContext = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuthContext must be used within AuthProvider");
  }
  return context;
};

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [auth, setAuth] = useState<AuthState | null>(() => {
    const stored = localStorage.getItem("auth");
    return stored ? JSON.parse(stored) : null;
  });

  const login = async (data: LoginDto) => {
    await AuthService.login(data, setAuth);
  };

  const signup = async (data: RegisterDto) => {
    await AuthService.signup(data, setAuth);
  };

  const logout = () => {
    AuthService.logout(setAuth);
  };

  const value = { auth, login, signup, logout };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
