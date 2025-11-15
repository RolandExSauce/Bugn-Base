import { createContext, useState, useContext } from "react";
import type { AuthState } from "../types/models";
import { mockUser } from "../types/temp/PlaceholderData";

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
  // temporarily setting to mock admin
  const [auth, setAuth] = useState<AuthState | undefined>({
    user: mockUser,
    accessToken: "",
    role: "ADMIN",
  });

  const value: AuthContextType = {
    auth,
    setAuth,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
