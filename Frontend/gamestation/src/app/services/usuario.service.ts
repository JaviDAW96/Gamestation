// src/app/services/usuario.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Usuario } from '../interfaces/Usuario';// Asegúrate de que la ruta sea correcta

@Injectable({ providedIn: 'root' })
export class UsuarioService {
  private baseUrl = 'https://localhost:8080/api/usuarios';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Usuario[]> {
    return this.http.get<Usuario[]>(this.baseUrl);
  }

  getById(id: number): Observable<Usuario> {
    return this.http.get<Usuario>(`${this.baseUrl}/${id}`);
  }

  create(usuario: Usuario): Observable<Usuario> {
    return this.http.post<Usuario>(this.baseUrl, usuario);
  }

  update(id: number, usuario: Usuario): Observable<Usuario> {
    return this.http.put<Usuario>(`${this.baseUrl}/${id}`, usuario);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}
