// src/app/components/home/home.component.ts
import { Component, OnInit } from '@angular/core';
import { HeaderComponent } from "../header/header.component";
import { FooterComponent } from "../footer/footer.component";
import { PostService } from '../services/posts.service';
import { Post } from '../interfaces/Post';
import { CommonModule } from '@angular/common';
import { PostListComponent } from '../post-list-component/post-list-component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  imports: [HeaderComponent, FooterComponent, CommonModule, PostListComponent]
})
export class HomeComponent implements OnInit {
  posts: Post[] = [];
  articulos: Post[] = [];
  errorMsg = '';
  showWelcome = true;
  username = '';

  constructor(private postService: PostService) {}

  ngOnInit(): void {
    this.postService.findAll('post').subscribe({
      next: (posts: Post[]) => {
        this.posts = posts;
        console.log(this.posts);
      },
      error: () => this.errorMsg = 'Error cargando los posts'
    });

    this.postService.findAll('articulo').subscribe({
      next: (articulos: Post[]) => this.articulos = articulos,
      error: () => this.errorMsg = 'Error cargando los artículos'
    });

    // Obtén el nombre del usuario (ajusta según tu lógica)
    this.username = localStorage.getItem('username') || '';

    setTimeout(() => {
      this.showWelcome = false;
    }, 3000); // 3 segundos
  }
}
