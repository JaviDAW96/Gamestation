// src/app/components/analista/analista.component.ts
import { Component, OnInit } from '@angular/core';

import { HttpClient } from '@angular/common/http';

import { ActivatedRoute, Router } from '@angular/router';
import { Post } from '../interfaces/Post';
// Make sure 'Analista' is exported in InterfazAnalista.ts, otherwise import the correct member
import { InterfazAnalista } from '../interfaces/InterfazAnalista';
import { PostService } from '../services/posts.service';
import { CommonModule } from '@angular/common';
import { AnalistaService } from '../services/analista.service';
// If the export is default, use:
// import Analista from '../interfaces/InterfazAnalista';

@Component({
  selector: 'app-analista',
  imports: [CommonModule],
  standalone: true,
  templateUrl: './analista-perfil.component.html',
  styleUrls: ['./analista-perfil.component.css']
})
export class AnalistaComponent implements OnInit {
  analista!: InterfazAnalista;
  analisis: Post[] = [];
  articulos: Post[] = [];
  noticias: Post[] = [];
  loading = true;
  errorMsg = '';

  constructor(
    private http: HttpClient,
    private postService: PostService,
    private route: ActivatedRoute,
    private router: Router,
    private analistaService: AnalistaService
  ) {}

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.analistaService.getPerfil(id).subscribe({
      next: a => this.analista = a,
      error: () => this.errorMsg = 'Error cargando perfil'
    });

    this.analistaService.getPostsPorTipo(id).subscribe({
      next: ({ analisis, articulos, noticias }) => {
        this.analisis = analisis ?? [];
        this.articulos = articulos ?? [];
        this.noticias = noticias ?? [];
        this.loading = false;
      },
      error: () => this.errorMsg = 'Error cargando los posts'
    });
  }


  crear(tipo: 'analisis'|'articulo'|'noticia') {
    this.router.navigate(['/analista', this.analista.id_usuario, 'posts', 'crear'], { queryParams: { tipo } });
  }

  editarPost(post: Post) {
    this.router.navigate(['/analista', this.analista.id_usuario, 'posts', post.id, 'editar']);
  }

  editarPerfil() {
    this.router.navigate(['/analista', this.analista.id_usuario, 'editar']);
  }
}