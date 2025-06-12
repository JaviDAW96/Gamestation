import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, FormArray } from '@angular/forms';
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
  imagenes: string[] = [];
  subiendoImagen = false;
  isDarkMode = false;


  constructor(
    private fb: FormBuilder,
    private postService: PostService,
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private location: Location

  ) {}

  ngOnInit() {
    this.postForm = this.fb.group({
      titulo: ['', Validators.required],
      subtitulo: [''],
      descripcion: ['', Validators.required],
      contenido: [{ value: '', disabled: true }, [Validators.required, Validators.minLength(10)]],
      fechaPublicacion: [{ value: '', disabled: false }, Validators.required],
      tipo: ['', Validators.required],
      imagenes: this.fb.array([], [
        Validators.required,
        this.miniaturaValidator,
        this.noDuplicateRolesValidator
      ])
    });

    // Deshabilita imágenes y contenido hasta que se seleccione tipo
    this.postForm.get('imagenes')?.disable();

    this.postForm.get('tipo')?.valueChanges.subscribe(tipo => {
      const contenidoCtrl = this.postForm.get('contenido');
      const imagenesCtrl = this.postForm.get('imagenes');
      if (tipo) {
        contenidoCtrl?.enable({ emitEvent: false });
        imagenesCtrl?.enable({ emitEvent: false });
      } else {
        contenidoCtrl?.disable({ emitEvent: false });
        imagenesCtrl?.disable({ emitEvent: false });
      }
      if (tipo === 'noticia') {
        contenidoCtrl?.setValidators([Validators.required, Validators.minLength(10), Validators.maxLength(5000)]);
      } else {
        contenidoCtrl?.setValidators([Validators.required, Validators.minLength(10)]);
      }
      // Fuerza la actualización de validadores y estado
      contenidoCtrl?.updateValueAndValidity({ onlySelf: true, emitEvent: false });
    });

    this.route.paramMap.subscribe(params => {
      const idParam = params.get('id');
      this.modoEdicion = !!idParam && this.route.snapshot.url.some(seg => seg.path === 'editar');
      if (this.modoEdicion && idParam) {
        this.cargando = true;
        this.postId = +idParam;
        this.loadPost(this.postId);
        // Habilita el campo en edición
        this.postForm.get('fechaPublicacion')?.enable();
      } else {
        // Establece la fecha actual y deshabilita el campo en creación
        const today = new Date().toISOString().split('T')[0];
        this.postForm.patchValue({ fechaPublicacion: today });
        this.postForm.get('fechaPublicacion')?.disable();
        this.modoEdicion = false;
        this.cargando = false;
      }
    });

    this.syncDarkMode();
  }

  private loadPost(id: number) {
    this.postService.getById(id).subscribe({
      next: (p: Post) => {
        this.postForm.patchValue({
          titulo: p.titulo,
          subtitulo: p.subtitulo,
          contenido: p.contenido,
          descripcion: p.descripcion,
          fechaPublicacion: p.fechaPublicacion ? p.fechaPublicacion.split('T')[0] : new Date().toISOString().split('T')[0],
          tipo: p.tipo,
        });

        // Limpia el FormArray
        this.imagenesFA.clear();

        // Map de roles por id (por compatibilidad con tu backend actual)
        const rolesPorId: Record<number, string> = {};
        if (p.miniatura) rolesPorId[p.miniatura.id] = 'miniatura';
        if (p.portada) rolesPorId[p.portada.id] = 'portada';
        if (p.imagenContenido1) rolesPorId[p.imagenContenido1.id] = 'contenido1';
        if (p.imagenContenido2) rolesPorId[p.imagenContenido2.id] = 'contenido2';
        if (p.imagenContenido3) rolesPorId[p.imagenContenido3.id] = 'contenido3';

        // Añade todas las imágenes al FormArray
        if (p.imagenes && p.imagenes.length) {
          p.imagenes.forEach(img => {
            const id = img.multimediaId;
            const url = img.multimedia?.url;
            const rol = img.rol || '';
            this.imagenesFA.push(this.fb.group({
              id: [id, Validators.required],
              url: [url, Validators.required],
              rol: [rol, Validators.required]
            }));
          });
        }

        this.cargando = false;
      },
      error: () => {
        this.errorMsg = 'Error al cargar el post';
        this.cargando = false;
      }
    });
  }


  onSubmit() {
    if (this.postForm.invalid) {
      this.postForm.markAllAsTouched();
      Swal.fire('Error', 'Revisa el formulario', 'error');
      return;
    }


    let wasDisabled = false;
    const fechaCtrl = this.postForm.get('fechaPublicacion');
    if (!this.modoEdicion && fechaCtrl?.disabled) {
      fechaCtrl.enable();
      wasDisabled = true;
    }

    this.postForm.patchValue({ imagenes: this.imagenesFA.value });

    const imagenesPayload = this.imagenesFA.value as Array<any>;
    const imagenesFiltradas = imagenesPayload.filter(img => img.id && img.url);

    const postData = {
      ...this.postForm.value,
      imagenes: imagenesFiltradas.map(img => ({
        multimediaId: img.id,
        rol: img.rol ?? ''
      })),
      miniaturaId: imagenesFiltradas.find(i => i.rol === 'miniatura')?.id ?? null,
      portadaId: imagenesFiltradas.find(i => i.rol === 'portada')?.id ?? null,
      imagenContenido1Id: imagenesFiltradas.find(i => i.rol === 'contenido1')?.id ?? null,
      imagenContenido2Id: imagenesFiltradas.find(i => i.rol === 'contenido2')?.id ?? null,
      imagenContenido3Id: imagenesFiltradas.find(i => i.rol === 'contenido3')?.id ?? null,
      multimediaIds: imagenesFiltradas.map(i => i.id),
      usuarioId: this.authService.getCurrentUserId ? this.authService.getCurrentUserId() : null
    };

    // Para depuración
    console.log('Payload enviado al crear/editar post:', JSON.stringify(postData, null, 2));

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
  
  async onFileChange(event: any) {
    const files: FileList = event.target.files;
    if (!files?.length) return;

    const tipo = this.postForm.get('tipo')?.value;
    const maxImagenes = tipo === 'noticia' ? 3 : 5;

    if (this.imagenesFA.length + files.length > maxImagenes) {
      Swal.fire('Límite de imágenes', `Solo puedes subir hasta ${maxImagenes} imágenes para este tipo de post.`, 'warning');
      event.target.value = '';
      return;
    }

    this.subiendoImagen = true;
    const rolesPorDefecto = ['miniatura', 'portada', 'contenido1', 'contenido2', 'contenido3'];

    let uploads = 0;
    for (let file of Array.from(files)) {
      const reader = new FileReader();
      reader.onload = async (e: any) => {
        const urlPreview = e.target.result as string;
        // 1. Sube a Cloudinary
        const cloudinaryData = await this.subirImagenACloudinary(file);

        // 2. Registra en tu backend y obtén el ID de tu BBDD
        this.postService.uploadImageToBackend({
          url: cloudinaryData.url,
          tipoContenido: cloudinaryData.tipo,
          nombre: cloudinaryData.nombre
        }).subscribe({
          next: (backendImg) => {
            const idxRol = this.imagenesFA.length;
            this.imagenesFA.push(this.fb.group({
              id:  [ backendImg.id,  Validators.required ], // <-- ID de tu BBDD
              url: [ backendImg.url, Validators.required ],
              rol: [ rolesPorDefecto[idxRol] || '', Validators.required ]
            }));
            uploads++;
            if (uploads === files.length) {
              this.subiendoImagen = false;
              this.postForm.markAllAsTouched();
            }
          },
          error: () => {
            this.subiendoImagen = false;
            Swal.fire('Error', 'No se pudo registrar la imagen en el servidor', 'error');
          }
        });
      };
      reader.readAsDataURL(file);
    }
  }


  async subirImagenACloudinary(file: File): Promise<{ url: string, tipo: string, nombre: string }> {
  const formData = new FormData();
  formData.append('file', file);
  formData.append('upload_preset', 'unsigned_preset');

  const res = await fetch('https://api.cloudinary.com/v1_1/dr0lc3jsc/image/upload', {
    method: 'POST',
    body: formData
  });
  const data = await res.json();
  return {
    url: data.secure_url,
    tipo: data.resource_type,
    nombre: data.original_filename
  };
}

  imagenesValidator(form: FormGroup) {
    return form.get('imagenesCount')?.value > 0 ? null : { imagenesRequired: true };
  }

  miniaturaValidator = (control: import('@angular/forms').AbstractControl) => {
    const fa = control as FormArray;
    const hasMini = fa.controls.some(ctrl => ctrl.value.rol === 'miniatura');
    return hasMini ? null : { noMiniatura: true };
  };

  noDuplicateRolesValidator = (control: import('@angular/forms').AbstractControl) => {
    const fa = control as FormArray;
    const roles = fa.controls.map(ctrl => ctrl.value.rol);
    const unique = new Set(roles.filter(r => r));
    return unique.size === roles.length ? null : { duplicateRoles: true };
  };

  eliminarImagen(index: number) {
  this.imagenesFA.removeAt(index);
  this.postForm.updateValueAndValidity();
}

  onRolChange(index: number, event: Event) {
    const value = (event.target as HTMLSelectElement).value;
    this.imagenesFA.at(index).get('rol')?.setValue(value);
    this.postForm.updateValueAndValidity();
  }

  get imagenesFA(): FormArray {
    return this.postForm.get('imagenes') as FormArray;
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

