import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  // Use direct Keycloak URL since it's accessible from browser
  private keycloakUrl = 'http://localhost:8090/realms/conference-realm/protocol/openid-connect/token';

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<any> {
    const body = new URLSearchParams();
    body.set('grant_type', 'password');
    body.set('client_id', 'angular-frontend');
    body.set('username', username);
    body.set('password', password);

    // Don't use interceptor for login request
    return this.http.post(this.keycloakUrl, body.toString(), {
      headers: new HttpHeaders({
        'Content-Type': 'application/x-www-form-urlencoded'
      })
    }).pipe(
      tap((response: any) => {
        // Save token to localStorage
        localStorage.setItem('access_token', response.access_token);
        localStorage.setItem('refresh_token', response.refresh_token);
        console.log('Token saved successfully');
      })
    );
  }

  logout() {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
  }

  isLoggedIn(): boolean {
    return !!localStorage.getItem('access_token');
  }

  getToken(): string | null {
    return localStorage.getItem('access_token');
  }
}
