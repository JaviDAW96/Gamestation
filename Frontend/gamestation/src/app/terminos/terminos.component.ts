import { Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';

@Component({
  selector: 'app-terminos',
  imports: [CommonModule],
  templateUrl: './terminos.component.html',
  styleUrl: './terminos.component.css'
})
export class TerminosComponent implements OnInit {
  constructor(private location: Location) {
    
  }

    isDarkMode = false;


  volver() {
    this.location.back();
  }

  syncDarkMode() {
    this.isDarkMode = document.body.classList.contains('dark-mode') ||
      localStorage.getItem('darkMode') === 'true';
  }

  toggleDarkMode() {
    document.body.classList.toggle('dark-mode');
    this.isDarkMode = document.body.classList.contains('dark-mode');
    localStorage.setItem('darkMode', this.isDarkMode ? 'true' : 'false');
  }

  ngOnInit() {
    this.syncDarkMode();
  }
}
