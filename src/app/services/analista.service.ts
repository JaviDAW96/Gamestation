import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { InterfazAnalista } from '../interfaces/InterfazAnalista';
import { Observable, forkJoin } from 'rxjs';
import { Post } from '../interfaces/Post';
import { PostService } from './posts.service';

@Injectable({ providedIn: 'root' })
export class AnalistaService {
    private apiUrl = 'http://localhost:8080/api/analistas';

    analista?: InterfazAnalista;
    errorMsg?: string;

    analisis: Post[] = [];
    articulos: Post[] = [];
    noticias: Post[] = [];
    loading: boolean = false;

    constructor(
        private http: HttpClient,
        private postService: PostService
    ) { }

    getPerfil(id: number): Observable<InterfazAnalista> {
        return this.http.get<InterfazAnalista>(`${this.apiUrl}/${id}`);
    }

    getPostsPorTipo(usuarioId: number) {
        return forkJoin({
            analisis: this.postService.getByUsuarioYTipo(usuarioId, 'analisis'),
            articulos: this.postService.getByUsuarioYTipo(usuarioId, 'articulo'),
            noticias: this.postService.getByUsuarioYTipo(usuarioId, 'noticia')
        });
    }

    private loadPerfil(id: number) {
        this.http.get<InterfazAnalista>(`http://localhost:8080/api/analistas/${id}`)
            .subscribe({
                next: a => this.analista = a,
                error: () => this.errorMsg = 'Error cargando perfil'
            });
    }

    private loadPosts(usuarioId: number) {
        Promise.all([
            this.postService.getByUsuarioYTipo(usuarioId, 'analisis').toPromise(),
            this.postService.getByUsuarioYTipo(usuarioId, 'articulo').toPromise(),
            this.postService.getByUsuarioYTipo(usuarioId, 'noticia').toPromise()
        ])
            .then(([an, ar, no]) => {
                this.analisis = an ?? [];
                this.articulos = ar ?? [];
                this.noticias = no ?? [];
                this.loading = false;
            })
            .catch(() => this.errorMsg = 'Error cargando los posts');
    }

}



