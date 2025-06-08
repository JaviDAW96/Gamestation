import { Component, OnInit } from '@angular/core';
import { Post } from '../interfaces/Post';
import { PostService } from '../services/posts.service';
import { AuthService } from '../services/auth.service';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from "../header/header.component";
import { FooterComponent } from "../footer/footer.component";

@Component({
  selector: 'app-articulos',
  standalone: true,
  imports: [CommonModule, RouterModule, HeaderComponent, FooterComponent],
  templateUrl: './articulos.component.html',
  styleUrl: './articulos.component.css'
})
export class ArticulosComponent implements OnInit {
  articulos: Post[] = [];
  articulosPage = 1;
  pageSize = 8;
  esAnalista = false;

  constructor(
    private postService: PostService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.postService.findAll('articulo').subscribe(data => {
      this.articulos = data;
    });
    this.esAnalista = this.authService.isAnalista?.() ?? false;
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
