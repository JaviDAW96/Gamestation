// src/app/components/usuario/usuario.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Usuario } from '../interfaces/Usuario';
import { UsuarioService } from '../services/usuario.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';
import { Location } from '@angular/common';
import { HeaderComponent } from "../header/header.component";
import { FooterComponent } from "../footer/footer.component";

@Component({
  selector: 'app-usuario',
  imports: [CommonModule, ReactiveFormsModule, HeaderComponent, FooterComponent],
  standalone: true,
  templateUrl: './usuario-perfil.component.html',
  styleUrls: ['./usuario-perfil.component.css']
})
export class UsuarioComponent implements OnInit {
  usuarioForm!: FormGroup;
  modoEdicion = false;
  usuarioId!: number;
  usuario?: Usuario; // <-- Añade esta línea
  cargando = true;
  errorMsg = '';

  avatarList: string[] = [
    'https://res.cloudinary.com/dr0lc3jsc/image/upload/v1749641777/Tifa_70_ojj5bg.png',
    'https://res.cloudinary.com/dr0lc3jsc/image/upload/v1749641770/Tifa_21_v3pxse.png',
    'https://res.cloudinary.com/dr0lc3jsc/image/upload/v1749641747/Aerith_36_b7s8vi.png',
    'https://res.cloudinary.com/dr0lc3jsc/image/upload/v1749641721/Aerith_2_ja7sbe.png',
    'https://res.cloudinary.com/dr0lc3jsc/image/upload/v1749641715/Aerith_49_f8mng5.png',
    'https://res.cloudinary.com/dr0lc3jsc/image/upload/v1749641678/Chris_Redfield_-_11_ofddz3.jpg',
    'https://res.cloudinary.com/dr0lc3jsc/image/upload/v1749641676/Chris_Redfield_-_10_rctx6l.jpg',
    'https://res.cloudinary.com/dr0lc3jsc/image/upload/v1749641657/Ashley_-_02_wk2ns1.jpg',
    'https://res.cloudinary.com/dr0lc3jsc/image/upload/v1749641653/Ashley_-_01_euamcs.jpg',
    'https://res.cloudinary.com/dr0lc3jsc/image/upload/v1749641639/Claire_Redfield_11_uxjabo.jpg',
    'https://res.cloudinary.com/dr0lc3jsc/image/upload/v1749641636/Claire_Redfield_6_cvrsjd.jpg',
    'https://res.cloudinary.com/dr0lc3jsc/image/upload/v1749641624/Ada_Wong_34_k0nruh.jpg',
    'https://res.cloudinary.com/dr0lc3jsc/image/upload/v1749641622/Ada_Wong_1_cqdxls.png',
    'https://res.cloudinary.com/dr0lc3jsc/image/upload/v1749641611/Leon_Kennedy_-_18_rznww1.jpg',
    'https://res.cloudinary.com/dr0lc3jsc/image/upload/v1749641606/Leon_Kennedy_-_16_oyoshs.jpg',
    'https://res.cloudinary.com/dr0lc3jsc/image/upload/v1749641531/BOTW-Link_avojxx.jpg',
    'https://res.cloudinary.com/dr0lc3jsc/image/upload/v1749499355/pkvkvv7hyenjmnmaar38.jpg',
    'https://res.cloudinary.com/dr0lc3jsc/image/upload/v1749498651/ridewulgpfzo97gz6psw.jpg',
    'https://res.cloudinary.com/dr0lc3jsc/image/upload/v1749490452/vvnfwrfzvjohw8tixcyt.jpg'
  ];

  constructor(
    private fb: FormBuilder,
    private usuarioService: UsuarioService,
    private route: ActivatedRoute,
    private router: Router,
    private location: Location
  ) {}

  ngOnInit(): void {
    this.usuarioForm = this.fb.group({
      nombre: ['', Validators.required],
      apellidos: ['', Validators.required],
      dni: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      fechaNacimiento: ['', Validators.required],
      imagen: ['', Validators.required] 
    });

    const idParam = this.route.snapshot.paramMap.get('id');
    if (idParam) {
      this.modoEdicion = true;
      this.usuarioId = Number(idParam);
      this.loadUsuario(this.usuarioId);
    } else {
      this.cargando = false;
    }

    this.route.queryParams.subscribe(params => {
      if (params['welcome']) {
       
        Swal.fire({
          icon: 'success',
          title: '¡Bienvenido!',
          text: 'Has iniciado sesión correctamente.'
        });
        this.router.navigate([], {
          relativeTo: this.route,
          queryParams: { welcome: null },
          queryParamsHandling: 'merge',
          replaceUrl: true
        });
      }
    });
  }

  private loadUsuario(id: number) {
    this.usuarioService.getById(id).subscribe({
      next: (u: Usuario) => {
        this.usuario = u; 

        this.usuarioForm.patchValue({
          nombre: u.nombre,
          apellidos: u.apellidos,
          dni: u.dni,
          email: u.email,
          fechaNacimiento: u.fechaNacimiento,
          imagen: u.imagen || ''
        });
        this.cargando = false;
      },
      error: () => {
        this.errorMsg = 'Error al cargar el usuario';
        this.cargando = false;
      }
    });
  }
  

  onSubmit() {
    if (this.usuarioForm.invalid) return;

    const datos: Usuario = {
      ...this.usuarioForm.value,
      id: this.usuarioId
    };

    const peticion$ = this.modoEdicion
      ? this.usuarioService.update(this.usuarioId, datos)
      : this.usuarioService.create(datos);

    peticion$.subscribe({
      next: () => this.router.navigate(['/usuarios']),
      error: () => this.errorMsg = 'Error al guardar usuario'
    });
  }

  onDelete() {
    if (!this.modoEdicion) return;
    if (!confirm('¿Seguro que deseas eliminar este usuario?')) return;

    this.usuarioService.delete(this.usuarioId).subscribe({
      next: () => this.router.navigate(['/usuarios']),
      error: () => this.errorMsg = 'Error al eliminar usuario'
    });
  }

  abrirModalEditarUsuario() {
    this.usuarioForm.patchValue({
      nombre: this.usuario?.nombre || '',
      apellidos: this.usuario?.apellidos || '',
      dni: this.usuario?.dni || '',
      email: this.usuario?.email || '',
      fechaNacimiento: this.usuario?.fechaNacimiento || '',
      imagen: this.usuario?.imagen || this.avatarList[0] 
    });
  }

  guardarCambiosUsuario() {
    if (this.usuarioForm.invalid || !this.usuario) return;

    const usuarioId = this.usuario.id;

    Swal.fire({
      title: '¿Guardar cambios?',
      text: '¿Estás seguro de que quieres actualizar tu perfil?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí, guardar',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        const datos = { ...this.usuarioForm.value };


        if (!datos.password) {
          delete datos.password;
        }

        const usuarioActualizado = { ...this.usuario, ...datos };
        if (!datos.password && 'password' in usuarioActualizado) {
          delete usuarioActualizado.password;
        }

        this.usuarioService.updateUsuario(usuarioId, usuarioActualizado).subscribe({
          next: actualizado => {
            this.usuario = actualizado;

            // Cierra el modal de Bootstrap
            const modal = document.getElementById('editarUsuarioModal');
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

  volver() {
    this.location.back();
  }
}
