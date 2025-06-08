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

  // Guarda el usuario autenticado (puedes adaptarlo a tu modelo)
  private currentUser: any = null;

  // ← Inyecta HttpClient
  constructor(private http: HttpClient) {}

  login(email: string, password: string) {
  return this.http
    .post<AuthResponse>(`${this.base}/login`, { email, password })
    .pipe(
      tap((res: any) => {
        this.saveToken(res.token);
        if (res.nombre) {
          localStorage.setItem('username', res.nombre);
        }
        // Soporta rol como string o como objeto
        let rolStr = '';
        if (typeof res.rol === 'string') {
          rolStr = res.rol;
        } else if (res.rol && typeof res.rol.rol === 'string') {
          rolStr = res.rol.rol;
        }
        if (rolStr) {
          localStorage.setItem('rol', rolStr.toLowerCase());
        }
        this.setCurrentUser(res.usuario); // Asegúrate de que 'usuario' viene en la respuesta
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
    dni: string;
    fecha_nacimiento: string;
    id_rol?: number; // Opcional, si tu backend lo pone por defecto puedes omitirlo
  }) {
    return this.http.post(`${this.base}/registro`, userData);
  }

  private saveToken(token: string): void {
    localStorage.setItem('token', token);
  }

  // Llama a esto al hacer login
  setCurrentUser(user: any) {
    this.currentUser = user;
    localStorage.setItem('currentUser', JSON.stringify(user));
  }

  // Recupera el usuario (por ejemplo, tras recargar la página)
  getCurrentUser() {
    if (!this.currentUser) {
      const userStr = localStorage.getItem('currentUser');
      if (userStr) this.currentUser = JSON.parse(userStr);
    }
    return this.currentUser;
  }

  // Devuelve solo el ID del usuario autenticado
  getCurrentUserId(): number | null {
    const user = this.getCurrentUser();
    return user ? user.id : null;
  }

   logout(): void {
    // Elimina el token y el rol del localStorage
    localStorage.removeItem('token');
    localStorage.removeItem('rol');

  }

  isAnalista(): boolean {
    const rol = localStorage.getItem('rol');
    return rol === 'analista';
  }
}

