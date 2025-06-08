// src/app/components/analista/analista.component.ts
import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Post } from '../interfaces/Post';
import { InterfazAnalista } from '../interfaces/InterfazAnalista';
import { PostService } from '../services/posts.service';
import { CommonModule } from '@angular/common';
import { AnalistaService } from '../services/analista.service';
import { HeaderComponent } from "../header/header.component";
import { FooterComponent } from "../footer/footer.component";
import { Location } from '@angular/common';

@Component({
  selector: 'app-analista',
  imports: [CommonModule, HeaderComponent, FooterComponent, RouterModule],
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
  paginaAnalisis = 1;
  paginaArticulos = 1;
  paginaNoticias = 1;

  constructor(
    private http: HttpClient,
    private postService: PostService,
    private route: ActivatedRoute,
    private router: Router,
    private analistaService: AnalistaService,
    private location: Location
  ) {}

  ngOnInit(): void {
    const analistaId = Number(this.route.snapshot.paramMap.get('id'));
    this.loading = true;
    this.analistaService.getPerfil(analistaId).subscribe({
      next: a => {
        this.analista = a;
        const posts = a.posts || [];
        this.analisis = posts.filter(p => p.tipo === 'analisis');
        this.articulos = posts.filter(p => p.tipo === 'articulo');
        this.noticias = posts.filter(p => p.tipo === 'noticia');
        this.loading = false;
      },
      error: () => {
        this.errorMsg = 'Error cargando perfil';
        this.loading = false;
      }
    });
  }

  crear(tipo: 'analisis'|'articulo'|'noticia') {
    this.router.navigate(['/analista', this.analista.id, 'posts', 'crear'], { queryParams: { tipo } });
  }

  editarPost(post: Post) {
    this.router.navigate(['/analista', this.analista.id, 'posts', post.id, 'editar']);
  }

  editarPerfil() {
    this.router.navigate(['/analista', this.analista.id, 'editar']);
  }

  getPaginationArray(total: number, pageSize: number): any[] {
    const pages = Math.ceil(total / pageSize);
    return Array(pages);
  }

  getTotalPages(arr: any[]): number {
    return Math.ceil(arr.length / 3);
  }

 volver() {
    this.location.back();
  }

}