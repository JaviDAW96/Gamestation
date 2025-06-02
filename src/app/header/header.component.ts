import { Component } from '@angular/core';

@Component({
  selector: 'app-header',
  imports: [],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  logout() {
    // Borra el token o la sesión
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    // Redirige al login
    window.location.href = '/login';
  }
}
