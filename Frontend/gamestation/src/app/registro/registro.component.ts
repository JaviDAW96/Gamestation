import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { CommonModule } from '@angular/common';
import { FooterComponent } from "../footer/footer.component";
import { HeaderComponent } from "../header/header.component";
import Swal from 'sweetalert2';

@Component({
  selector: 'app-register',
  templateUrl: './registro.component.html',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule],
  styleUrls: ['./registro.component.css']
})
export class RegistroComponent implements OnInit {
  registroForm!: FormGroup;
  errorMessage: string | null = null;
  successMessage: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.registroForm = this.fb.nonNullable.group({
      nombre: ['', Validators.required],
      apellidos: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', Validators.required],
      dni: ['', Validators.required],
      fecha_nacimiento: ['', Validators.required]
    }, { validators: this.passwordsMatch });
  }

  // Valida que password y confirmPassword coincidan
  private passwordsMatch(control: import('@angular/forms').AbstractControl) {
    const group = control as FormGroup;
    const pass = group.get('password')?.value;
    const confirm = group.get('confirmPassword')?.value;
    return pass === confirm ? null : { notMatching: true };
  }

  get nombre() { return this.registroForm.get('nombre'); }
  get apellidos() { return this.registroForm.get('apellidos'); }
  get email()    { return this.registroForm.get('email'); }
  get password() { return this.registroForm.get('password'); }
  get confirmPassword() { return this.registroForm.get('confirmPassword'); }

  onSubmit(): void {
    this.errorMessage = this.successMessage = null;
    if (this.registroForm.invalid) {
      Swal.fire({
        icon: 'error',
        title: 'Formulario inválido',
        text: 'Por favor, completa todos los campos correctamente.'
      });
      return;
    }

    const { nombre, apellidos, email, password, dni, fecha_nacimiento } = this.registroForm.value;

    this.authService.registro({
      nombre,
      apellidos,
      email,
      password,
      dni,
      fecha_nacimiento,
    }).subscribe({
      next: () => {
        Swal.fire({
          icon: 'success',
          title: '¡Registro exitoso!',
          text: 'Tu cuenta ha sido creada correctamente.'
        }).then(() => {
          this.router.navigate(['/login']);
        });
      },
      error: err => {
        Swal.fire({
          icon: 'error',
          title: 'Error en el registro',
          text: err?.error?.message || 'No se pudo crear la cuenta. Intenta de nuevo.'
        });
      }
    });
  }
}
