import { createContext, useState, useContext } from "react";
import type { AuthState } from "../types/models";

type AuthContextType = {
  setAuth: React.Dispatch<React.SetStateAction<AuthState | undefined>>;
  auth: AuthState | undefined;
};

type AuthContextProviderProps = {
  children: React.ReactNode;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

// eslint-disable-next-line react-refresh/only-export-components
export const useAuthContext = (): AuthContextType => {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error(
      "useAuthContext must be used within an AuthContextProvider"
    );
  }
  return context;
};

export const AuthContextProvider = ({ children }: AuthContextProviderProps) => {
  const [auth, setAuth] = useState<AuthState | undefined>(undefined);

  const value: AuthContextType = {
    auth,
    setAuth,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
