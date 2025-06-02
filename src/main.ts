// src/main.ts
import { enableProdMode } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';
import { provideHttpClient, withInterceptorsFromDi, HTTP_INTERCEPTORS } from '@angular/common/http';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { importProvidersFrom } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';

import { environment } from './environments/environment';
import { AppComponent } from './app/app.component';
import { routes } from './app/app.routes';
import { JwtInterceptor } from './app/services/jwt.interceptor';

if (environment.production) {
  enableProdMode();
}

bootstrapApplication(AppComponent, {
  providers: [
    // Registra HttpClient correctamente
    provideHttpClient(withInterceptorsFromDi()),
    // Formularios reactivos
    importProvidersFrom(ReactiveFormsModule),
    // Configuración del router
    provideRouter(routes, withComponentInputBinding()),
    { provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true }
  ]
}).catch(err => console.error(err));
