import { Component } from '@angular/core';
import { Location } from '@angular/common';

@Component({
  selector: 'app-terminos',
  imports: [],
  templateUrl: './terminos.component.html',
  styleUrl: './terminos.component.css'
})
export class TerminosComponent {
  constructor(private location: Location) {}

  volver() {
    this.location.back();
  }
}
