// src/app/components/usuario/usuario.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Usuario } from '../interfaces/Usuario';
import { UsuarioService } from '../services/usuario.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';
import { Location } from '@angular/common';

@Component({
  selector: 'app-usuario',
  imports: [CommonModule, ReactiveFormsModule],
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
      imagen: [''],
      password: ['', Validators.required]
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
        // Muestra la alerta de bienvenida
        Swal.fire({
          icon: 'success',
          title: '¡Bienvenido!',
          text: 'Has iniciado sesión correctamente.'
        });
        // Elimina el parámetro 'welcome' de la URL sin recargar
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
        this.usuario = u; // <-- Añade esta línea
        // Poblamos el formulario (password no se rellena por seguridad)
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
    if (this.usuario) {
      this.usuarioForm.patchValue({
        nombre: this.usuario.nombre,
        apellidos: this.usuario.apellidos,
        dni: this.usuario.dni,
        email: this.usuario.email,
        fechaNacimiento: this.usuario.fechaNacimiento,
        imagen: this.usuario.imagen
      });
    }
  }

  guardarCambiosUsuario() {
    if (this.usuarioForm.invalid || !this.usuario) return;

    const usuarioId = this.usuario.id; // <-- Guarda el id aquí

    Swal.fire({
      title: '¿Guardar cambios?',
      text: '¿Estás seguro de que quieres actualizar tu perfil?',
      icon: 'question',
      showCancelButton: true,
      confirmButtonText: 'Sí, guardar',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        const datos = this.usuarioForm.value;
        const usuarioActualizado = { ...this.usuario, ...datos };

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
