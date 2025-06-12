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

   toggleDarkMode() {
  document.body.classList.toggle('dark-mode');
  this.isDarkMode = document.body.classList.contains('dark-mode');
}

ngOnInit() {
  this.isDarkMode = document.body.classList.contains('dark-mode');
}
}
