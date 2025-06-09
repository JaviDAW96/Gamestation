import { Component, Input, OnChanges, OnInit } from '@angular/core';
import { Post } from '../interfaces/Post';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { AuthService } from '../services/auth.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-post-list',
  templateUrl: './post-list-component.html',
  styleUrls: ['./post-list-component.css'],
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  standalone: true  
})
export class PostListComponent implements OnChanges, OnInit {
  @Input() items: Post[] = [];
  @Input() soloColumna = false;

  noticiasGrandes: Post[] = [];
  noticiasPequenas: Post[] = [];
  analisisDestacados: Post[] = [];
  articulosDestacados: Post[] = [];

  currentUserId: number | null = null;
  esAnalista: boolean = false;

  constructor(
    private location: Location,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit() {
    this.currentUserId = this.authService.getCurrentUserId();
    this.esAnalista = this.authService.isAnalista?.() ?? false;

    // Obtén el usuario actual (ajusta según tu AuthService)
    const usuario = this.authService.getCurrentUser?.();

    this.route.queryParams.subscribe(params => {
      if (params['welcome']) {
        Swal.fire({
          icon: 'success',
          title: `¡Bienvenidx, ${usuario?.nombre || ''}!`,
          text: 'Has iniciado sesión correctamente.'
        });
        this.router.navigate([], {
          relativeTo: this.route,
          queryParams: { welcome: null },
          queryParamsHandling: 'merge',
          replaceUrl: true
        });
      }
    });
  }

  ngOnChanges() {
    // Filtra noticias y destacados
    const noticias = this.items.filter(i => i.tipo === 'noticia');
    this.noticiasGrandes = noticias.slice(0, 2);
    this.noticiasPequenas = noticias.slice(2, 5);

    this.analisisDestacados = this.items
      .filter(i => i.tipo === 'analisis')
      .slice(0, 3);

    this.articulosDestacados = this.items
      .filter(i => i.tipo === 'articulo')
      .slice(0, 3);
  }
}

