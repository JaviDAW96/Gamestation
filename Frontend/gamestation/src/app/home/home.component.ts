// src/app/components/home/home.component.ts
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import Swal from 'sweetalert2';
import { HeaderComponent } from "../header/header.component";
import { FooterComponent } from "../footer/footer.component";
import { PostService } from '../services/posts.service';
import { Post } from '../interfaces/Post';
import { CommonModule } from '@angular/common';
import { PostListComponent } from '../post-list-component/post-list-component';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  imports: [HeaderComponent, FooterComponent, CommonModule, PostListComponent]
})
export class HomeComponent implements OnInit {
  posts: Post[] = [];
  noticias: Post[] = [];
  analisis: Post[] = [];
  articulos: Post[] = [];
  errorMsg = '';
  showWelcome = true;
  username = '';

  analisisPage = 1;
  articulosPage = 1;
  pageSize = 8; 

  constructor(
    private postService: PostService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    forkJoin([
      this.postService.findAll('noticia'),
      this.postService.findAll('analisis'),
      this.postService.findAll('articulo')
    ]).subscribe(([noticias, analisis, articulos]) => {
      this.posts = [...noticias, ...analisis, ...articulos];
    });

    this.username = localStorage.getItem('username') || '';

    this.route.queryParams.subscribe(params => {
      if (params['welcome'] === '1') {
        Swal.fire({
          icon: 'success',
          title: `¡Bienvenido${this.username ? ', ' + this.username : ''}!`,
          showConfirmButton: false,
          timer: 2500
        });
      }
    });
  }

  get analisisPages(): number[] {
    return Array(Math.ceil(this.analisis.length / this.pageSize)).fill(0).map((_, i) => i + 1);
  }
  get articulosPages(): number[] {
    return Array(Math.ceil(this.articulos.length / this.pageSize)).fill(0).map((_, i) => i + 1);
  }
  get analisisPaginados(): Post[] {
    const start = (this.analisisPage - 1) * this.pageSize;
    return this.analisis.slice(start, start + this.pageSize);
  }
  get articulosPaginados(): Post[] {
    const start = (this.articulosPage - 1) * this.pageSize;
    return this.articulos.slice(start, start + this.pageSize);
  }
}
