import { Component } from '@angular/core';
import { CommonModule, Location } from '@angular/common';

@Component({
  selector: 'app-privacidad',
  imports: [CommonModule],
  templateUrl: './privacidad.component.html',
  styleUrls: ['./privacidad.component.css'] 
})
export class PrivacidadComponent {
  constructor(private location: Location) {}


  isDarkMode = false;

  volver() {
    this.location.back();
  }

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

}
