import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Component({
  selector: 'app-videojuego-perfil',
  imports: [],
  templateUrl: './videojuego-perfil.component.html',
  styleUrl: './videojuego-perfil.component.css'
})
export class VideojuegoPerfilComponent {
    private apiUrl = `${environment.apiUrl}/games`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<VideojuegoPerfilComponent[]> {
    return this.http.get<VideojuegoPerfilComponent[]>(this.apiUrl);
  }

  getById(id: number): Observable<VideojuegoPerfilComponent> {
    return this.http.get<VideojuegoPerfilComponent>(`${this.apiUrl}/${id}`);
  }

  create(game: VideojuegoPerfilComponent): Observable<VideojuegoPerfilComponent> {
    return this.http.post<VideojuegoPerfilComponent>(this.apiUrl, game);
  }

  update(id: number, game: VideojuegoPerfilComponent): Observable<VideojuegoPerfilComponent> {
    return this.http.put<VideojuegoPerfilComponent>(`${this.apiUrl}/${id}`, game);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}

