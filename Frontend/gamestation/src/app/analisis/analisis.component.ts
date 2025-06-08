import { Component, OnInit } from '@angular/core';
import { Post } from '../interfaces/Post';
import { PostService } from '../services/posts.service';
import { AuthService } from '../services/auth.service';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from "../header/header.component";
import { FooterComponent } from "../footer/footer.component";

@Component({
  selector: 'app-analisis',
  standalone: true,
  imports: [CommonModule, RouterModule, HeaderComponent, FooterComponent],
  templateUrl: './analisis.component.html',
  styleUrl: './analisis.component.css'
})
export class AnalisisComponent implements OnInit {
  analisis: Post[] = [];
  analisisPage = 1;
  pageSize = 8;
  esAnalista = false;

  constructor(
    private postService: PostService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.postService.findAll('analisis').subscribe(data => {
      this.analisis = data;
    });
    this.esAnalista = this.authService.isAnalista?.() ?? false;
  }

  get analisisPages(): number[] {
    return Array(Math.ceil(this.analisis.length / this.pageSize))
      .fill(0)
      .map((_, i) => i + 1);
  }

  get analisisPaginados(): Post[] {
    const start = (this.analisisPage - 1) * this.pageSize;
    return this.analisis.slice(start, start + this.pageSize);
  }
}
