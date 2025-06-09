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
  selector: 'app-noticias',
  standalone: true,
  imports: [CommonModule, RouterModule, HeaderComponent, FooterComponent, ReactiveFormsModule],
  templateUrl: './noticias.component.html',
  styleUrl: './noticias.component.css'
})
export class NoticiasComponent implements OnInit {
  noticias: Post[] = [];
  noticiasPage = 1;
  pageSize = 8;
  esAnalista = false;
  busquedaControl = new FormControl(''); 
    sinResultados = false; 

  constructor(
    private postService: PostService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.cargarNoticias();
    this.esAnalista = this.authService.isAnalista?.() ?? false;
  }

  cargarNoticias(): void {
    this.postService.findAll('noticia').subscribe((data: Post[]) => {
      this.noticias = data;
      this.noticiasPage = 1;
    });
  }

  buscar(): void {
    const query = this.busquedaControl.value?.trim() || '';
    if (!query) {
      this.cargarNoticias();
      this.sinResultados = false;
      return;
    }
    this.postService.search(query, 'noticia').subscribe((posts: Post[]) => {
      this.noticias = posts;
      this.noticiasPage = 1;
      this.sinResultados = posts.length === 0; // <-- Esto es clave
    });
  }

  get noticiasPages(): number[] {
    return Array(Math.ceil(this.noticias.length / this.pageSize))
      .fill(0)
      .map((_, i) => i + 1);
  }

  get noticiasPaginadas(): Post[] {
    const start = (this.noticiasPage - 1) * this.pageSize;
    return this.noticias.slice(start, start + this.pageSize);
  }
}
