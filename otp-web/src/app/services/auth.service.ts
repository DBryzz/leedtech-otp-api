import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface LoginRequest {
    email: string;
    password: string;
}

export interface AuthResponse {
    token: string;
    message: string;
    success: boolean;
    user: any;
}

export interface RegisterRequest {
    email: string;
    password: string;
    firstName: string;
    lastName: string;
}

@Injectable({
    providedIn: 'root'
})
export class AuthService {
    private apiUrl = 'http://localhost:8081/api/v1/auth';
    private tokenKey = 'authToken';
    private tokenExpiryKey = 'tokenExpiry';
    private defaultExpiryMs = 3600000; // 1 hour fallback

    constructor(private http: HttpClient) {}

    login(credentials: LoginRequest): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${this.apiUrl}/authenticate`, credentials).pipe(
            tap(response => {
                if (response.success && response.token) {
                    localStorage.setItem(this.tokenKey, response.token);
                    const expiryTime = Date.now() + this.defaultExpiryMs;
                    localStorage.setItem(this.tokenExpiryKey, expiryTime.toString());
                }
            })
        );
    }

    register(request: RegisterRequest): Observable<AuthResponse> {
        return this.http.post<AuthResponse>(`${this.apiUrl}/register`, request);
    }

    logout(): void {
        localStorage.removeItem(this.tokenKey);
        localStorage.removeItem(this.tokenExpiryKey);
    }

    getToken(): string | null {
        const token = localStorage.getItem(this.tokenKey);
        if (!token) return null;
        
        if (this.isTokenExpired()) {
            this.logout();
            return null;
        }
        return token;
    }

    isAuthenticated(): boolean {
        return !!this.getToken();
    }

    private isTokenExpired(): boolean {
        const expiry = localStorage.getItem(this.tokenExpiryKey);
        if (!expiry) return true;
        return Date.now() >= parseInt(expiry, 10);
    }
}
