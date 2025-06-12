import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { Location } from '@angular/common';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-contacto',
  templateUrl: './contacto.component.html',
  styleUrls: ['./contacto.component.css'],
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule]
})
export class ContactoComponent implements OnInit {
  contactForm: FormGroup;
  mensajeEnviado = false;

  constructor(private fb: FormBuilder, private authService: AuthService, private location: Location) {
    const usuario = this.authService.getCurrentUser() || {};

    this.contactForm = this.fb.group({
      nombre: [usuario.nombre || '', Validators.required],
      correo: [usuario.email || '', [Validators.required, Validators.email]],
      datos: ['', Validators.required]
    });
  }

  isDarkMode = false;

  ngOnInit() {
    this.isDarkMode = document.body.classList.contains('dark-mode') ||
      localStorage.getItem('darkMode') === 'true';
  }

  enviarFormulario() {
    if (this.contactForm.valid) {
      this.mensajeEnviado = true;
      this.contactForm.reset({
        nombre: this.contactForm.get('nombre')?.value,
        correo: this.contactForm.get('correo')?.value,
        datos: ''
      });
      Swal.fire({
        icon: 'success',
        title: '¡Solicitud enviada!',
        text: 'Hemos recibido su solicitud, cuando nuestros técnicos estén disponibles se pondrán en contacto con usted en breve.\n\nGracias por contactar con GameStation',
        confirmButtonColor: '#00aaff'
      });
    }
  }

  volver() {
    this.location.back();
  }

  toggleDarkMode() {
    document.body.classList.toggle('dark-mode');
    this.isDarkMode = document.body.classList.contains('dark-mode');
  }

}
