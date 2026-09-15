import { createContext, useContext, useEffect, useMemo, useState } from "react";
import authService from "../services/authService";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    async function loadCurrentUser() {
        try {
            const currentUser = await authService.getCurrentUser();
            setUser(currentUser);
            return currentUser;
        } catch (error) {
            setUser(null);
            return null;
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        loadCurrentUser();
    }, []);

    async function login(username, password) {
        const response = await authService.login({ username, password });
        const currentUser = await loadCurrentUser();
        return { response, user: currentUser };
    }

    async function logout() {
        try {
            await authService.logout();
        } finally {
            setUser(null);
        }
    }

    const value = useMemo(() => ({
        user,
        setUser,
        loading,
        isAuthenticated: Boolean(user),
        role: user?.role || null,
        login,
        logout,
        loadCurrentUser
    }), [user, loading]);

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
    const context = useContext(AuthContext);
    if (!context) {
        throw new Error("useAuth phải được sử dụng bên trong AuthProvider");
    }
    return context;
}
