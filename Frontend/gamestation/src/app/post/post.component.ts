// src/app/components/post/post.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Post, PostTipo } from '../../app/interfaces/Post';
import { PostService } from '../services/posts.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../services/auth.service';
import { Location } from '@angular/common';
import Swal from 'sweetalert2';

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
  imagenes: string[] = []; // <-- URLs de Cloudinary
  imagenesPreview: string[] = [];
  imagenesIds: number[] = [];
  subiendoImagen = false;
  rolesImagenes: string[] = []; // Inicializa el array

  constructor(
    private fb: FormBuilder,
    private postService: PostService,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private location: Location

  ) {}

  ngOnInit(): void {
    this.postForm = this.fb.group({
      titulo: ['', Validators.required],
      subtitulo: [''],
      contenido: ['', [Validators.required, Validators.minLength(10)]],
      descripcion: ['', Validators.required],
      fechaPublicacion: ['', Validators.required],
      tipo: ['', Validators.required]
    }, { validators: [this.imagenesValidator.bind(this)] });

    this.cargando = false; // Por defecto, no está cargando

    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      this.modoEdicion = !!idParam && this.route.snapshot.url.some(seg => seg.path === 'editar');
      if (this.modoEdicion && idParam) {
        this.cargando = true;
        this.postId = +idParam;
        this.loadPost(this.postId);
      } else {
        this.modoEdicion = false;
        this.cargando = false;
      }
    });
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

        // --- Cargar portada como imagen principal ---
        this.imagenes = [];
        this.imagenesPreview = [];
        this.imagenesIds = [];
        if (p.portada) {
          this.imagenes = [p.portada.url];
          this.imagenesPreview = [p.portada.url];
          this.imagenesIds = [p.portada.id];
        }

        this.cargando = false;
      },
      error: () => {
        this.errorMsg = 'Error al cargar el post';
        this.cargando = false;
      }
    });
  }

  private getIdByRol(rol: string): number | null {
    const idx = this.rolesImagenes.indexOf(rol);
    return idx !== -1 ? this.imagenesIds[idx] : null;
  }

  onSubmit() {
    if (this.postForm.invalid) {
      this.postForm.markAllAsTouched();
      Swal.fire({
        icon: 'error',
        title: 'Formulario inválido',
        text: 'Por favor, completa todos los campos obligatorios correctamente.'
      });
      return;
    }
    if (this.subiendoImagen || this.imagenesIds.length !== this.imagenes.length) return;

    const id_usuario = this.authService.getCurrentUserId();
    if (!id_usuario) {
      this.errorMsg = 'No se pudo obtener el ID del usuario. Inicia sesión nuevamente.';
      return;
    }

    const postData = {
      ...this.postForm.value,
      usuarioId: id_usuario,
      miniaturaId: this.getIdByRol('miniatura'),
      portadaId: this.getIdByRol('portada'),
      imagenContenido1Id: this.getIdByRol('contenido1'),
      imagenContenido2Id: this.getIdByRol('contenido2'),
      imagenContenido3Id: this.getIdByRol('contenido3'),
    };

    const peticion$ = this.modoEdicion
      ? this.postService.update(this.postId, postData)
      : this.postService.create(postData);

    peticion$.subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: this.modoEdicion ? '¡Post actualizado!' : '¡Post creado!',
          text: this.modoEdicion
            ? 'Los cambios se han guardado correctamente.'
            : 'El post se ha creado correctamente.'
        }).then(() => {
          this.router.navigate(['/']);
        });
      },
      error: () => this.errorMsg = 'Error al guardar el post'
    });
  }

  onDelete() {
    if (!this.modoEdicion) return;
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'Esta acción eliminará el post de forma permanente.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        this.postService.delete(this.postId).subscribe({
          next: () => {
            Swal.fire({
              icon: 'success',
              title: '¡Post eliminado!',
              text: 'El post ha sido eliminado correctamente.'
            }).then(() => {
              this.router.navigate(['/']);
            });
          },
          error: () => this.errorMsg = 'Error al eliminar el post'
        });
      }
    });
  }

   volver() {
    this.location.back();
  }
  
  onFileChange(event: any) {
    const input = event.target as HTMLInputElement;
    if (!input.files) return;

    let files = Array.from(input.files);
    const espacioDisponible = 5 - this.imagenes.length;
    if (espacioDisponible <= 0) return;

    files = files.slice(0, espacioDisponible);
    this.subiendoImagen = true;

    for (let file of files) {
      const formData = new FormData();
      formData.append('file', file);
      formData.append('upload_preset', 'unsigned_preset');

      fetch('https://api.cloudinary.com/v1_1/dr0lc3jsc/image/upload', {
        method: 'POST',
        body: formData
      })
        .then(res => res.json())
        .then(data => {
          const multimedia = {
            url: data.secure_url,
            tipoContenido: 'image/jpeg', 
            nombre: '', 
            descripcion: '',
            fechaSubida: new Date().toISOString() 
          };

          this.postService.createMultimedia(multimedia).subscribe({
            next: (resp) => {
              this.imagenes.push(data.secure_url);
              this.imagenesPreview.push(data.secure_url);
              this.imagenesIds.push(resp.id); 
              this.rolesImagenes.push('miniatura'); 
              this.postForm.updateValueAndValidity();
              if (this.imagenes.length === this.imagenesPreview.length) {
                this.subiendoImagen = false;
              }
            }
          });
        });
    }
  }

  imagenesValidator(form: FormGroup) {
    return this.imagenes.length > 0 ? null : { imagenesRequired: true };
  }

  eliminarImagen(index: number) {
    this.imagenes.splice(index, 1);
    this.imagenesPreview.splice(index, 1);
    this.imagenesIds.splice(index, 1);
    this.rolesImagenes.splice(index, 1); // Elimina también el rol correspondiente
    this.postForm.updateValueAndValidity();
  }

  onRolChange(index: number, event: Event) {
    const value = (event.target as HTMLSelectElement).value;

    // Si ya existe ese rol en otra imagen, lo quitamos de la anterior
    const prevIdx = this.rolesImagenes.findIndex((rol, i) => rol === value && i !== index);
    if (prevIdx !== -1) {
      this.rolesImagenes[prevIdx] = ''; // O asigna un valor por defecto, o fuerza a elegir otro rol
    }

    this.rolesImagenes[index] = value;
    this.postForm.updateValueAndValidity();
  }
}

