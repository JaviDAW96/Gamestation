import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router'; 
import Swal from 'sweetalert2';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterModule], 
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {

  isAnalista: boolean = false;
  username: string = '';
  userId: string | null = null;
  analistaId: string | null = null;
  isDarkMode = false;

  constructor(private authService: AuthService, private router: Router) {
    const token = localStorage.getItem('token');
    const rol = localStorage.getItem('rol');
    this.isAnalista = !!token && rol === 'analista';
    this.username = localStorage.getItem('username') || ''; 
    this.userId = localStorage.getItem('userId'); 

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
  
        this.authService.logout();
        this.router.navigate(['/login']);
        Swal.fire('Sesión cerrada', 'Has cerrado sesión correctamente.', 'success');
      }
    });
  }

  toggleDarkMode() {
    document.body.classList.toggle('dark-mode');
    this.isDarkMode = document.body.classList.contains('dark-mode');
    localStorage.setItem('darkMode', this.isDarkMode ? 'true' : 'false');
  }

  ngOnInit() {
    this.syncDarkMode();
  }

  syncDarkMode() {
    this.isDarkMode = document.body.classList.contains('dark-mode') ||
      localStorage.getItem('darkMode') === 'true';
  }
}
