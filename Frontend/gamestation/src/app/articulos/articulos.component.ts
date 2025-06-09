import { Component, OnInit } from '@angular/core';
import { Post } from '../interfaces/Post';
import { PostService } from '../services/posts.service';
import { AuthService } from '../services/auth.service';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from "../header/header.component";
import { FooterComponent } from "../footer/footer.component";
import { FormControl, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-articulos',
  standalone: true,
  imports: [CommonModule, RouterModule, HeaderComponent, FooterComponent, ReactiveFormsModule],
  templateUrl: './articulos.component.html',
  styleUrl: './articulos.component.css'
})
export class ArticulosComponent implements OnInit {
  articulos: Post[] = [];
  articulosPage = 1;
  pageSize = 8;
  esAnalista = false;
  busquedaControl = new FormControl('');
  sinResultados = false; // Añade esto

  constructor(
    private postService: PostService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.cargarArticulos();
    this.esAnalista = this.authService.isAnalista?.() ?? false;
  }

  cargarArticulos(): void {
    this.postService.findAll('articulo').subscribe((data: Post[]) => {
      this.articulos = data;
      this.articulosPage = 1;
      this.sinResultados = false;
    });
  }

  buscar(): void {
    const query = this.busquedaControl.value?.trim() || '';
    if (!query) {
      this.cargarArticulos();
      return;
    }
    this.postService.search(query, 'articulo').subscribe((posts: Post[]) => {
      this.articulos = posts;
      this.articulosPage = 1;
      this.sinResultados = posts.length === 0; // Marca si no hay resultados
    });
  }

  get articulosPages(): number[] {
    return Array(Math.ceil(this.articulos.length / this.pageSize))
      .fill(0)
      .map((_, i) => i + 1);
  }

  get articulosPaginados(): Post[] {
    const start = (this.articulosPage - 1) * this.pageSize;
    return this.articulos.slice(start, start + this.pageSize);
  }
}
