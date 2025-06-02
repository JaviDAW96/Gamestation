// src/app/components/post/post.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Post, PostTipo } from '../../app/interfaces/Post';
import { PostService } from '../services/posts.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-post',
  imports: [CommonModule, ReactiveFormsModule],
  standalone: true,
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {
  postForm!: FormGroup;
  modoEdicion = false;
  postId!: number;
  cargando = true;
  errorMsg = '';
  tipos: PostTipo[] = ['analisis', 'articulo', 'noticia'];

  constructor(
    private fb: FormBuilder,
    private postService: PostService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.postForm = this.fb.group({
      titulo: ['', Validators.required],
      subtitulo: [''],
      contenido: ['', [Validators.required, Validators.minLength(10)]],
      descripcion: ['', Validators.required],
      fechaPublicacion: ['', Validators.required],
      tipo: ['', Validators.required],
      id_usuario: [null, Validators.required]
    });

    const idParam = this.route.snapshot.paramMap.get('postId');
    if (idParam) {
      this.modoEdicion = true;
      this.postId = Number(idParam);
      this.loadPost(this.postId);
    } else {
      this.cargando = false;
    }
  }

  private loadPost(id: number) {
    this.postService.getById(id).subscribe({
      next: (p: Post) => {
        this.postForm.patchValue({
          titulo: p.titulo,
          subtitulo: p.subtitulo,
          contenido: p.contenido,
          descripcion: p.descripcion,
          fechaPublicacion: p.fechaPublicacion,
          tipo: p.tipo,
          id_usuario: p.id_usuario
        });
        this.cargando = false;
      },
      error: () => {
        this.errorMsg = 'Error al cargar el post';
        this.cargando = false;
      }
    });
  }

  onSubmit() {
    if (this.postForm.invalid) return;

    const datos: Post = this.postForm.value;

    const peticion$ = this.modoEdicion
      ? this.postService.update(this.postId, datos)
      : this.postService.create(datos);

    peticion$.subscribe({
      next: () => this.router.navigate(['/analista', datos.id_usuario]),
      error: () => this.errorMsg = 'Error al guardar el post'
    });
  }

  onDelete() {
    if (!this.modoEdicion) return;
    if (!confirm('¿Seguro que deseas eliminar este post?')) return;
    this.postService.delete(this.postId).subscribe({
      next: () => {
        const uid = this.postForm.value.id_usuario;
        this.router.navigate(['/analista', uid]);
      },
      error: () => this.errorMsg = 'Error al eliminar el post'
    });
  }
}

