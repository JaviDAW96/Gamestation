// src/app/services/post.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Post } from '../interfaces/Post';
import { environment } from '../../environments/environment';
import { FormControl } from '@angular/forms';

@Injectable({ providedIn: 'root' })
export class PostService {
  private baseUrl = `${environment.apiUrl}/posts`;
  analisis: Post[] = [];
  analisisPage: number = 1;
  articulos: Post[] = [];
  noticias: Post[] = [];
  busquedaControl: FormControl = new FormControl('');


  constructor(private http: HttpClient) {}


  findAll(tipo: string): Observable<Post[]> {
    const params = new HttpParams().set('tipo', tipo);
    return this.http.get<Post[]>(this.baseUrl, { params });
  }

  getById(id: number): Observable<Post> {
    return this.http.get<Post>(`${this.baseUrl}/${id}`);
  }

  getByUsuarioYTipo(usuarioId: number, tipo: string): Observable<Post[]> {
    const params = new HttpParams()
      .set('usuarioId', usuarioId.toString()) // <-- AQUÍ
      .set('tipo', tipo);
    return this.http.get<Post[]>(this.baseUrl, { params });
  }

  create(post: any): Observable<Post> {
    return this.http.post<Post>(this.baseUrl, post);
  }

  update(id: number, post: any): Observable<Post> {
    return this.http.put<Post>(`${this.baseUrl}/${id}`, post);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/${id}`);
  }

  createMultimedia(multimedia: any) {
    return this.http.post<any>(`${environment.apiUrl}/multimedia`, multimedia);
  }

  findAllPosts(): Observable<Post[]> {
    return this.http.get<Post[]>(this.baseUrl);
  }

  logAnalista(analista: any) {
    console.log('Analista recibido:', analista);
    console.log('ID usuario:', analista.id_usuario);
  }


    cargarAnalisis() {
    this.findAll('analisis').subscribe(data => {
      this.analisis = data;
      this.analisisPage = 1;
    });
  }
  
  search(q: string, tipo?: string): Observable<Post[]> {
    let params = new HttpParams().set('q', q);
    if (tipo) params = params.set('tipo', tipo);
    return this.http.get<Post[]>(`${this.baseUrl}/search`, { params });
  }

  uploadImageToBackend(multimedia: { url: string, tipoContenido: string, nombre: string }) {
    return this.http.post<{ id: number, url: string }>(
      `${environment.apiUrl}/imagenes`, // <-- BIEN, usa la ruta de tu backend
      multimedia
    );
  }
}