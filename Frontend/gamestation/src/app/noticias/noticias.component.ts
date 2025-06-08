import { Component, OnInit } from '@angular/core';
import { Post } from '../interfaces/Post';
import { PostService } from '../services/posts.service';
import { AuthService } from '../services/auth.service';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from "../header/header.component";
import { FooterComponent } from "../footer/footer.component";

@Component({
  selector: 'app-noticias',
  standalone: true,
  imports: [CommonModule, RouterModule, HeaderComponent, FooterComponent],
  templateUrl: './noticias.component.html',
  styleUrl: './noticias.component.css'
})
export class NoticiasComponent implements OnInit {
  noticias: Post[] = [];
  noticiasPage = 1;
  pageSize = 8;
  esAnalista = false;

  constructor(
    private postService: PostService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.postService.findAll('noticia').subscribe(data => {
      this.noticias = data;
    });
    this.esAnalista = this.authService.isAnalista?.() ?? false;
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
