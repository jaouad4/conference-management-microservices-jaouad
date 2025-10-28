import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive, FormsModule],
  template: `
    <header>
      <div class="container">
        <h1>🎤 Système de Gestion de Conférences</h1>
        <nav>
          <a routerLink="/" routerLinkActive="active" [routerLinkActiveOptions]="{exact: true}">Accueil</a>
          <a routerLink="/keynotes" routerLinkActive="active">Keynotes</a>
          <a routerLink="/conferences" routerLinkActive="active">Conférences</a>
          <div class="auth-section">
            <span *ngIf="!isLoggedIn()" class="login-form">
              <input [(ngModel)]="username" placeholder="Username" />
              <input [(ngModel)]="password" type="password" placeholder="Password" />
              <button (click)="login()">Login</button>
            </span>
            <span *ngIf="isLoggedIn()" class="logout-section">
              <span class="username">👤 {{username}}</span>
              <button (click)="logout()">Logout</button>
            </span>
          </div>
        </nav>
      </div>
    </header>
    <main class="container">
      <div *ngIf="loginError" class="error-message">❌ {{loginError}}</div>
      <div *ngIf="loginSuccess" class="success-message">✅ Connecté avec succès!</div>
      <router-outlet></router-outlet>
    </main>
  `,
  styles: [`
    .auth-section {
      margin-left: auto;
      display: flex;
      align-items: center;
      gap: 10px;
    }
    .login-form input {
      padding: 5px 10px;
      border: 1px solid #ddd;
      border-radius: 4px;
      margin-right: 5px;
    }
    .login-form button, .logout-section button {
      padding: 5px 15px;
      background: #007bff;
      color: white;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    .login-form button:hover, .logout-section button:hover {
      background: #0056b3;
    }
    .username {
      margin-right: 10px;
      font-weight: bold;
    }
    .error-message {
      background: #f8d7da;
      color: #721c24;
      padding: 10px;
      border-radius: 4px;
      margin-bottom: 20px;
    }
    .success-message {
      background: #d4edda;
      color: #155724;
      padding: 10px;
      border-radius: 4px;
      margin-bottom: 20px;
    }
    nav {
      display: flex;
      align-items: center;
      gap: 20px;
    }
  `]
})
export class AppComponent {
  title = 'Conference Management System';
  username: string = 'admin';
  password: string = 'admin';
  loginError: string = '';
  loginSuccess: boolean = false;

  constructor(public authService: AuthService) {}

  login() {
    this.loginError = '';
    this.loginSuccess = false;
    
    console.log('Attempting login with username:', this.username);
    
    this.authService.login(this.username, this.password).subscribe({
      next: (response) => {
        console.log('Login successful!', response);
        this.loginSuccess = true;
        this.password = '';
        setTimeout(() => this.loginSuccess = false, 3000);
        // Force reload keynotes after login
        window.location.reload();
      },
      error: (err) => {
        console.error('Login error details:', err);
        if (err.status === 0) {
          this.loginError = 'Impossible de contacter Keycloak. CORS error?';
        } else if (err.status === 401) {
          this.loginError = 'Identifiants incorrects.';
        } else {
          this.loginError = 'Échec de connexion: ' + (err.error?.error_description || err.message);
        }
      }
    });
  }

  logout() {
    this.authService.logout();
    this.username = 'admin';
    this.password = 'admin';
  }

  isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }
}
