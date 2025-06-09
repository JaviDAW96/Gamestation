import { Component } from '@angular/core';
import { Location } from '@angular/common';

@Component({
  selector: 'app-privacidad',
  imports: [],
  templateUrl: './privacidad.component.html',
  styleUrls: ['./privacidad.component.css'] // Corregido: styleUrls en plural
})
export class PrivacidadComponent {
  constructor(private location: Location) {}

  volver() {
    this.location.back();
  }
}
