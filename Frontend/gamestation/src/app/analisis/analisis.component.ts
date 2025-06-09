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
  selector: 'app-analisis',
  standalone: true,
  imports: [CommonModule, RouterModule, HeaderComponent, FooterComponent, ReactiveFormsModule],
  templateUrl: './analisis.component.html',
  styleUrl: './analisis.component.css'
})
export class AnalisisComponent implements OnInit {
  analisis: Post[] = [];
  analisisPage = 1;
  pageSize = 8;
  esAnalista = false;
  busquedaControl = new FormControl(''); 
  sinResultados = false; 

  constructor(
    private postService: PostService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.cargarAnalisis();
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

  buscar() {
    const query = this.busquedaControl.value?.trim() || '';
    if (!query) {
      this.cargarAnalisis();
      this.sinResultados = false;
      return;
    }
    this.postService.search(query, 'analisis').subscribe((posts: Post[]) => {
      this.analisis = posts;
      this.analisisPage = 1;
      this.sinResultados = posts.length === 0;
    });
  }

  cargarAnalisis(): void {
    this.postService.findAll('analisis').subscribe((data: Post[]) => {
      this.analisis = data;
      this.analisisPage = 1;
    });
  }
}
