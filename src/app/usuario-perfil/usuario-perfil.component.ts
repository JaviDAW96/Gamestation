// src/app/components/usuario/usuario.component.ts
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Usuario } from '../interfaces/Usuario';
import { UsuarioService } from '../services/usuario.service';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';

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
  cargando = true;
  errorMsg = '';

  constructor(
    private fb: FormBuilder,
    private usuarioService: UsuarioService,
    private route: ActivatedRoute,
    private router: Router
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
  }

  private loadUsuario(id: number) {
    this.usuarioService.getById(id).subscribe({
      next: (u: Usuario) => {
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
}
