import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { AuthService } from './services/auth.service';
import {isNonNullArray} from "./lib/utils";

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [CommonModule, FormsModule, RouterOutlet, RouterLink, RouterLinkActive],
    template: `
        <nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-4">
            <div class="container">
                <a class="navbar-brand" href="#">OTP - One Time Payment</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarNav">
                    <ul class="navbar-nav ms-auto">
                        <li class="nav-item" *ngIf="!isAuthenticated">
                            <button class="btn btn-link nav-link" (click)="showLogin = true">Login</button>
                        </li>
                        <ng-container *ngIf="isAuthenticated">
                            <li class="nav-item">
                                <a class="nav-link" routerLink="/enroll" routerLinkActive="active">Enroll</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" routerLink="/payment" routerLinkActive="active">Make Payment</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" routerLink="/payments" routerLinkActive="active">View Payments</a>
                            </li>
                            <li class="nav-item">
                                <button class="btn btn-link nav-link" (click)="logout()">Logout</button>
                            </li>
                        </ng-container>
                    </ul>
                </div>
            </div>
        </nav>
        <div class="container">
            <router-outlet></router-outlet>
        </div>

        <!-- Login Modal -->
        <div class="modal show d-block" tabindex="-1" *ngIf="showLogin" style="background: rgba(0,0,0,0.5)">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Login</h5>
                        <button type="button" class="btn-close" (click)="showLogin = false"></button>
                    </div>
                    <div class="modal-body">
                        <div *ngIf="signupError" class="alert alert-danger">{{ signupError }}</div>
                        <form (ngSubmit)="onLogin()">
                            <div class="mb-3">
                                <label for="email" class="form-label">Email</label>
                                <input type="email" class="form-control" id="email" [(ngModel)]="loginEmail"
                                       name="email" required>
                            </div>
                            <div class="mb-3">
                                <label for="password" class="form-label">Password</label>
                                <input type="password" class="form-control" id="password" [(ngModel)]="loginPassword"
                                       name="password" required>
                            </div>
                            <button type="submit" class="btn btn-primary w-100" [disabled]="loginLoading">
                                <span *ngIf="loginLoading" class="spinner-border spinner-border-sm me-2"></span>
                                Login
                            </button>
                        </form>
                        <span class="sm">
                        Do not have an account? <a (click)="showSignup = true"> Sign up now! </a>
                            </span>
                    </div>
                </div>
            </div>
        </div>

        <!-- Signup Modal -->
        <div class="modal show d-block" tabindex="-1" *ngIf="showSignup" style="background: rgba(0,0,0,0.5)">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Signup</h5>
                        <button type="button" class="btn-close" (click)="showSignup = false"></button>
                    </div>
                    <div class="modal-body">
                        <div *ngIf="signupError" class="alert alert-danger">{{ signupError }}</div>
                        <form (ngSubmit)="onSignup()">
                            <div class="mb-3">
                                <label for="email" class="form-label">Email</label>
                                <input type="email" class="form-control" id="email" [(ngModel)]="loginEmail"
                                       name="email" required>
                            </div>
                            <div class="mb-3">
                                <label for="firstName" class="form-label">Firstname</label>
                                <input type="text" class="form-control" id="firstName" [(ngModel)]="fistName"
                                       name="firstName" required>
                            </div>
                            <div class="mb-3">
                                <label for="lastName" class="form-label">Lastname</label>
                                <input type="text" class="form-control" id="lastName" [(ngModel)]="lastName"
                                       name="lastName" required>
                            </div>
                            <div class="mb-3">
                                <label for="password" class="form-label">Password</label>
                                <input type="password" class="form-control" id="password" [(ngModel)]="loginPassword"
                                       name="password" required>
                            </div>
                            <button type="submit" class="btn btn-primary w-100" [disabled]="loginLoading">
                                <span *ngIf="loginLoading" class="spinner-border spinner-border-sm me-2"></span>
                                Sign Up
                            </button>
                        </form>
                        <span class="sm">
                        Have an account? <a (click)="showLogin = true"> Login! </a>
                            </span>
                    </div>
                </div>
            </div>
        </div>
    `
})
export class AppComponent {
    showLogin = false;
    showSignup = false;
    loginEmail = '';
    fistName = '';
    lastName = '';
    loginPassword = '';
    signupError = '';
    loginLoading = false;

    constructor(private authService: AuthService) {}

    get isAuthenticated(): boolean {
        return this.authService.isAuthenticated();
    }

    onLogin(): void {
        this.loginLoading = true;
        this.signupError = '';

        this.authService.login({ email: this.loginEmail, password: this.loginPassword }).subscribe({
            next: () => {
                this.loginLoading = false;
                this.showLogin = false;
                this.loginEmail = '';
                this.loginPassword = '';
            },
            error: (error) => {
                this.loginLoading = false;
                this.signupError = error.error?.detail || 'Login failed';
            }
        });
    }

    onSignup(): void {
        this.loginLoading = true;
        this.signupError = '';

        this.authService.register({ email: this.loginEmail, password: this.loginPassword, firstName: this.fistName, lastName: this.lastName }).subscribe({
            next: () => {
                this.loginLoading = false;
                this.showSignup = false;
                this.loginEmail = '';
                this.fistName = '';
                this.lastName = '';
                this.loginPassword = '';
            },
            error: (error) => {
                this.loginLoading = false;
                this.signupError = isNonNullArray(error.error?.invalidParams)  ? error.error?.invalidParams[0].reason : error.error?.detail;

            }
        });
    }

    logout(): void {
        this.authService.logout();
    }
}
