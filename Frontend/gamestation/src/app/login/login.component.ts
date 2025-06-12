import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';
import { AnalistaService } from '../services/analista.service'; // importa el servicio

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule,  ReactiveFormsModule, RouterModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']  // opcional, para tus estilos
})
export class LoginComponent implements OnInit {
  loginForm!: FormGroup;
  errorMessage: string | null = null;
  isDarkMode = false;
  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private analistaService: AnalistaService, // añade aquí
    private router: Router
  ) {}

  ngOnInit(): void {
    // Definimos el formulario con validaciones
    this.loginForm = this.fb.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  get username() { return this.loginForm.get('username'); }
  get password() { return this.loginForm.get('password'); }


    toggleDarkMode() {
    this.isDarkMode = !this.isDarkMode;
    if (this.isDarkMode) {
      document.body.classList.add('dark-mode');
      localStorage.setItem('darkMode', 'true');
    } else {
      document.body.classList.remove('dark-mode');
      localStorage.setItem('darkMode', 'false');
    }
  }

  onSubmit(): void {
    if (this.loginForm.invalid) {
      Swal.fire({
        icon: 'error',
        title: 'Formulario inválido',
        text: 'Por favor, completa todos los campos correctamente.'
      });
      return;
    }

    const username = this.loginForm.value.username;
    const password = this.loginForm.value.password;
    this.authService.login(username, password).subscribe({
      next: (res) => {
        // Guarda el userId y username en localStorage según la respuesta real
        if (res.usuario && res.usuario.id) {
          localStorage.setItem('userId', res.usuario.id);
          localStorage.setItem('username', res.usuario.nombre);
          localStorage.setItem('rol', res.usuario.rol?.rol || '');

          // Si es analista, guarda el analistaId
          if (res.usuario.rol?.rol === 'analista') {
            this.analistaService.getAnalistaPorUsuarioId(res.usuario.id).subscribe(analista => {
              localStorage.setItem('analistaId', analista.id.toString());
              // Aquí puedes navegar o mostrar el Swal si quieres esperar a que esté guardado
              Swal.fire({
                icon: 'success',
                title: '¡Bienvenido!',
                text: 'Has iniciado sesión correctamente.'
              }).then(() => {
                this.router.navigate(['/home'], { queryParams: { welcome: 1 } });
              });
            });
            return; // Evita que el Swal se muestre dos veces
          }
        }

        // Si no es analista, sigue como siempre
        Swal.fire({
          icon: 'success',
          title: '¡Bienvenido!',
          text: 'Has iniciado sesión correctamente.'
        }).then(() => {
          this.router.navigate(['/home'], { queryParams: { welcome: 1 } });
        });
      },
      error: (err) => {
        Swal.fire({
          icon: 'error',
          title: 'Error de autenticación',
          text: err?.error?.message || 'Usuario o contraseña incorrectos.'
        });
      }
    });
  }
}
