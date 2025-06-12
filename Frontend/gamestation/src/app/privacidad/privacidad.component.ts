import { Component, OnInit } from '@angular/core';
import { CommonModule, Location } from '@angular/common';

@Component({
  selector: 'app-privacidad',
  imports: [CommonModule],
  templateUrl: './privacidad.component.html',
  styleUrls: ['./privacidad.component.css'] 
})
export class PrivacidadComponent implements OnInit {
  constructor(private location: Location) {}


  isDarkMode = false;

  volver() {
    this.location.back();
  }

  ngOnInit() {
    this.syncDarkMode();
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
}
