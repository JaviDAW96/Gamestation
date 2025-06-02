import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';          // ← Importa HttpClient
import { catchError, tap } from 'rxjs/operators';
import { throwError } from 'rxjs';
import { environment } from '../../environments/environment'; // ← Importa environment

interface AuthResponse {
  token: string;
  // …otras props
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly base = environment.apiUrl + '/auth';    // URL centralizada

  // ← Inyecta HttpClient
  constructor(private http: HttpClient) {}

  login(nombre: string, password: string) {
    return this.http
      .post<AuthResponse>(`${this.base}/login`, { nombre, password })
      .pipe(
        tap((res: any) => {
          this.saveToken(res.token);
          if (res.nombre) {
            localStorage.setItem('username', res.nombre);
          }
        }),
        catchError(err => {
          console.error('Error en el login:', err);
          return throwError(() => new Error('Error al iniciar sesión'));
        })
      );
  }

  registro(userData: { 
    nombre: string; 
    apellidos: string; 
    email: string; 
    password: string; 
  }) {
    return this.http.post(`${this.base}/registro`, userData);
  }

  private saveToken(token: string): void {
    localStorage.setItem('token', token);
  }
}

