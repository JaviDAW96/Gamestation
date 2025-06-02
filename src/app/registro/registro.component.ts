import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { CommonModule } from '@angular/common';
import { FooterComponent } from "../footer/footer.component";
import { HeaderComponent } from "../header/header.component";

@Component({
  selector: 'app-register',
  templateUrl: './registro.component.html',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule, FooterComponent, HeaderComponent],
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
      confirmPassword: ['', Validators.required]
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
      this.registroForm.markAllAsTouched();
      return;
    }

    const { nombre, apellidos, email, password } = this.registroForm.value;

    this.authService.registro({ nombre, apellidos, email, password }).subscribe({
      next: () => {
        this.successMessage = 'Registro exitoso. Por favor, inicia sesión.';
        // opcionalmente redirigir tras un breve timeout:
        setTimeout(() => this.router.navigate(['/login']), 2000);
      },
      error: err => {
        this.errorMessage = err.error?.message || 'Error al registrarse';
      }
    });
  }
}
