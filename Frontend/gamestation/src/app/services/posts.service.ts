// src/app/services/post.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Post } from '../interfaces/Post';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class PostService {
  private baseUrl = `${environment.apiUrl}/posts`;

  constructor(private http: HttpClient) {}

  // Buscar todos por tipo
  findAll(tipo: string): Observable<Post[]> {
    const params = new HttpParams().set('tipo', tipo);
    return this.http.get<Post[]>(this.baseUrl, { params });
  }

  getById(id: number): Observable<Post> {
    return this.http.get<Post>(`${this.baseUrl}/${id}`);
  }

  getByUsuarioYTipo(usuarioId: number, tipo: string): Observable<Post[]> {
    const params = new HttpParams()
      .set('usuarioId', usuarioId.toString())
      .set('tipo', tipo);
    return this.http.get<Post[]>(this.baseUrl, { params });
  }

  create(post: Post): Observable<Post> {
    return this.http.post<Post>(this.baseUrl, post);
  }

  update(id: number, post: Post): Observable<Post> {
    return this.http.put<Post>(`${this.baseUrl}/${id}`, post);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }
}