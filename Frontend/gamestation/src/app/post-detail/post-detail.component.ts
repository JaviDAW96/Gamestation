import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PostService } from '../services/posts.service';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { Location } from '@angular/common';

@Component({
  selector: 'app-post-detail',
  templateUrl: './post-detail.component.html',
  styleUrls: ['./post-detail.component.css'],
  imports: [CommonModule, ReactiveFormsModule],
  standalone: true
})
export class PostDetailComponent implements OnInit {
  post: any;

  constructor(
    private route: ActivatedRoute,
    private postService: PostService,
    private location: Location
  ) {}

  isDarkMode = false;

  ngOnInit() {
    const idParam = this.route.snapshot.paramMap.get('id');
    const id = idParam !== null ? Number(idParam) : null;
    if (id !== null && !isNaN(id)) {
      this.postService.getById(id).subscribe(post => this.post = post);
    }
    
    this.syncDarkMode();
  }

  volver() {
    this.location.back();
  }

  getParrafos(contenido: string): string[] {
    return contenido ? contenido.split('\n') : [];
  }

  syncDarkMode() {
    this.isDarkMode = document.body.classList.contains('dark-mode') ||
      localStorage.getItem('darkMode') === 'true';
  }

  toggleDarkMode() {
    document.body.classList.toggle('dark-mode');
    this.isDarkMode = document.body.classList.contains('dark-mode');
    localStorage.setItem('darkMode', this.isDarkMode ? 'true' : 'false');
  }

}
