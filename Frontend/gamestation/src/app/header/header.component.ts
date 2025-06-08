import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router'; // <-- IMPORTANTE
import Swal from 'sweetalert2';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule], // <-- AGREGA RouterModule AQUÍ
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  // Propiedad para saber si el usuario es analista y está logueado
  isAnalista: boolean = false;
  username: string = '';
  userId: string | null = null;
  analistaId: string | null = null;

  constructor(private authService: AuthService, private router: Router) {
    const token = localStorage.getItem('token');
    const rol = localStorage.getItem('rol');
    this.isAnalista = !!token && rol === 'analista';
    this.username = localStorage.getItem('username') || ''; // 'username' es el nombre, no username real
    this.userId = localStorage.getItem('userId'); // id del usuario
    // Recupera el id de analista si existe en localStorage
    this.analistaId = localStorage.getItem('analistaId');
  }

  logout() {
    Swal.fire({
      title: '¿Cerrar sesión?',
      text: '¿Estás seguro de que deseas cerrar sesión?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonText: 'Sí, cerrar sesión',
      cancelButtonText: 'Cancelar'
    }).then((result) => {
      if (result.isConfirmed) {
        // Aquí tu lógica real de logout
        this.authService.logout();
        this.router.navigate(['/login']);
        Swal.fire('Sesión cerrada', 'Has cerrado sesión correctamente.', 'success');
      }
    });
  }
}
