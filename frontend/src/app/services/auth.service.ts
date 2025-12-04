import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

const AUTH_API = 'http://localhost:8080/api/auth/';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: HttpClient) { }

  login(credentials: any): Observable<any> {
    return this.http.post(AUTH_API + 'signin', {
      email: credentials.email,
      password: credentials.password,
      code: credentials.code
    }, httpOptions);
  }

  register(user: any): Observable<any> {
    return this.http.post(AUTH_API + 'signup', {
      name: user.name,
      email: user.email,
      password: user.password
    }, httpOptions);
  }

  setup2fa(): Observable<any> {
    return this.http.post(AUTH_API + 'setup-2fa', {}, httpOptions);
  }

  verify2fa(code: string): Observable<any> {
    return this.http.post(AUTH_API + 'verify-2fa', { code }, httpOptions);
  }
}
