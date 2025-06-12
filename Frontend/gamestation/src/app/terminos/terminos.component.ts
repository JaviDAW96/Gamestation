import { Component } from '@angular/core';
import { CommonModule, Location } from '@angular/common';

@Component({
  selector: 'app-terminos',
  imports: [CommonModule],
  templateUrl: './terminos.component.html',
  styleUrl: './terminos.component.css'
})
export class TerminosComponent {
  constructor(private location: Location) {
    
  }

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
