import axios, { type AxiosResponse } from "axios";
import { createContext, useState, useContext } from "react";
import type UserDto from "../types/temp/UserDto";
import type LoginDto from "../types/temp/LoginDto";

const LOGIN_URL = "/url";
const SIGNUP_URL = "/url";
const LOGOUT_URL = "/url";

interface AuthState {
  userId: string;
  accessToken: string;
}

type AuthContextType = {
  setAuth: React.Dispatch<React.SetStateAction<AuthState | undefined>>;
  auth: AuthState | undefined;
  login: (
    credentials: LoginDto
  ) => Promise<AxiosResponse<unknown, unknown> | undefined>;
  logout: () => void;
  signup: (
    credentials: UserDto
  ) => Promise<AxiosResponse<unknown, unknown> | undefined>;
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

  const login = async (
    credentials: LoginDto
  ): Promise<AxiosResponse<unknown, unknown> | undefined> => {
    const controller = new AbortController();
    const timeout = setTimeout(() => {
      controller.abort();
    }, 10000);

    try {
      const response = await axios.post(LOGIN_URL, credentials, {
        signal: controller.signal,
        withCredentials: true,
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (response.status === 200) {
        clearTimeout(timeout);
        setAuth({
          userId: response.data.userId,
          accessToken: response.data.token,
        });
      }
      return response;
    } catch (error) {
      console.error("Login failed: ", error);
      setAuth(undefined);
      clearTimeout(timeout);
      return;
    }
  };

  const signup = async (
    credentials: UserDto
  ): Promise<AxiosResponse<unknown, unknown> | undefined> => {
    const controller = new AbortController();
    setAuth(undefined);
    const timeout = setTimeout(() => {
      controller.abort();
    }, 10000);
    try {
      const response = await axios.post(SIGNUP_URL, credentials, {
        signal: controller.signal,
        withCredentials: true,
        headers: {
          "Content-Type": "application/json",
        },
      });
      if (response.status === 200) {
        clearTimeout(timeout);
        login({ email: credentials.email, password: credentials.password });
      }
      return response;
    } catch (error) {
      console.error("Signup failed: ", error);
      clearTimeout(timeout);
      return;
    }
  };

  const logout = async () => {
    try {
      await axios.post(LOGOUT_URL, {}, { withCredentials: true });
      setAuth(undefined);
    } catch (error) {
      console.error("Logout failed:", error);
    }
  };

  const value: AuthContextType = {
    auth,
    login,
    logout,
    signup,
    setAuth,
  };

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
