import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegistroComponent } from './registro/registro.component';
import { HomeComponent } from './home/home.component';
import { VideojuegoPerfilComponent } from './videojuego-perfil/videojuego-perfil.component';
import { UsuarioComponent } from './usuario-perfil/usuario-perfil.component';
import { AnalistaComponent } from './analista-perfil/analista-perfil.component';
import { AuthGuard } from './auth/auth.guard'; // Asegúrate de que la ruta sea correcta
// Agrega aquí los demás componentes que tengas, por ejemplo:
// import { OtroComponente } from './otro/otro.component';

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'registro', component: RegistroComponent },
    {
        path: '',
        canActivate: [AuthGuard],
        children: [
            { path: 'home', component: HomeComponent },
            { path: 'videojuego-perfil/:id', component: VideojuegoPerfilComponent },
            { path: 'usuario-perfil/:id', component: UsuarioComponent },
            { path: 'analista-perfil/:id', component: AnalistaComponent },
            { path: '', redirectTo: 'home', pathMatch: 'full' }, // Redirige a home si está autenticado y entra a /
        ]
    },
    { path: '**', redirectTo: 'login' } // Cualquier otra ruta, redirige a login
];
