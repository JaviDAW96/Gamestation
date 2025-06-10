// src/app/components/analista/analista.component.ts
import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { Post } from '../interfaces/Post';
import { InterfazAnalista } from '../interfaces/InterfazAnalista';
import { PostService } from '../services/posts.service';
import { CommonModule } from '@angular/common';
import { AnalistaService } from '../services/analista.service';
import { HeaderComponent } from "../header/header.component";
import { FooterComponent } from "../footer/footer.component";
import { Location } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-analista',
  imports: [CommonModule, HeaderComponent, FooterComponent, RouterModule, ReactiveFormsModule],
  standalone: true,
  templateUrl: './analista-perfil.component.html',
  styleUrls: ['./analista-perfil.component.css']
})
export class AnalistaComponent implements OnInit {
  analista!: InterfazAnalista;
  analisis: Post[] = [];
  articulos: Post[] = [];
  noticias: Post[] = [];
  loading = true;
  errorMsg = '';
  paginaAnalisis = 1;
  paginaArticulos = 1;
  paginaNoticias = 1;
  perfilForm!: FormGroup;

  constructor(
    private http: HttpClient,
    private postService: PostService,
    private route: ActivatedRoute,
    private router: Router,
    private analistaService: AnalistaService,
    private location: Location,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    const analistaId = Number(this.route.snapshot.paramMap.get('id'));
    this.loading = true;
    this.analistaService.getPerfil(analistaId).subscribe({
      next: a => {
        this.analista = a;
        const posts = a.posts || [];
        this.analisis = posts.filter(p => p.tipo === 'analisis');
        this.articulos = posts.filter(p => p.tipo === 'articulo');
        this.noticias = posts.filter(p => p.tipo === 'noticia');
        this.loading = false;

        
        this.perfilForm = this.fb.group({
          descripcion: [this.analista.descripcion, Validators.required],
          experienciaLaboral: [this.analista.experienciaLaboral, Validators.required],
          noticiasPublicadas: [this.analista.noticiasPublicadas, [Validators.required, Validators.min(0)]],
        });
      },
      error: () => {
        this.errorMsg = 'Error cargando perfil';
        this.loading = false;
      }
    });
  }

  crear(tipo: 'analisis'|'articulo'|'noticia') {
    this.router.navigate(['/analista', this.analista.id, 'posts', 'crear'], { queryParams: { tipo } });
  }

  editarPost(post: Post) {
    this.router.navigate(['/analista', this.analista.id, 'posts', post.id, 'editar']);
  }

  editarPerfil() {
    this.router.navigate(['/analista', this.analista.id, 'editar']);
  }

  getPaginationArray(total: number, pageSize: number): any[] {
    const pages = Math.ceil(total / pageSize);
    return Array(pages);
  }

  getTotalPages(arr: any[]): number {
    return Math.ceil(arr.length / 3);
  }

  volver() {
    this.location.back();
  }

  abrirModalEditarPerfil() {

    this.perfilForm.patchValue({
      descripcion: this.analista.descripcion,
      experienciaLaboral: this.analista.experienciaLaboral,
      noticiasPublicadas: this.analista.noticiasPublicadas,
    });
  }

  guardarCambios() {
    if (this.perfilForm.invalid || !this.analista) return;

    Swal.fire({
      title: '¿Guardar cambios?',
      text: '¿Estás seguro de que quieres actualizar tu perfil?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí, guardar',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        const datos = this.perfilForm.value;
        const analistaActualizado = { ...this.analista, ...datos };

        this.analistaService.updateAnalista(this.analista.id, analistaActualizado).subscribe({
          next: actualizado => {
            this.analista = actualizado;

            // Cierra el modal de Bootstrap
            const modal = document.getElementById('editarPerfilModal');
            if (modal) {
              // @ts-ignore
              bootstrap.Modal.getInstance(modal)?.hide();
            }

            // SweetAlert de éxito
            Swal.fire({
              icon: 'success',
              title: 'Perfil actualizado',
              text: 'Los cambios se han guardado correctamente',
              timer: 1800,
              showConfirmButton: false
            });
          },
          error: () => {
            Swal.fire('Error', 'No se pudo actualizar el perfil', 'error');
          }
        });
      }
    });
  }
}