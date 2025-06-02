import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http'; // Importar HttpClient
import { environment } from '../environments/environment';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly base = environment.apiUrl + '/auth';

  constructor(private http: HttpClient) {} // Inyectar HttpClient en el constructor

  login(username: string, password: string) {
    return this.http.post<{ token: string; user: { id: number; username: string } }>(`${this.base}/login`, { username, password });
  }

  register(data: { username: string; email: string; password: string }) {
    return this.http.post(`${this.base}/registro`, data);
  }
}