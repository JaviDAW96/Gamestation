import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegistroComponent } from './registro/registro.component';
import { HomeComponent } from './home/home.component';
import { VideojuegoPerfilComponent } from './videojuego-perfil/videojuego-perfil.component';
import { UsuarioComponent } from './usuario-perfil/usuario-perfil.component';
import { AnalistaComponent } from './analista-perfil/analista-perfil.component';
import { AuthGuard } from './auth/auth.guard'; // Asegúrate de que la ruta sea correcta
import { PostComponent } from './post/post.component'; // Asegúrate de importar el componente
import { PostDetailComponent } from './post-detail/post-detail.component'; // Asegúrate de importar el componente de detalle
import { AnalisisComponent } from './analisis/analisis.component'; // <-- Añade esta importación
import { ArticulosComponent } from './articulos/articulos.component';
import { NoticiasComponent } from './noticias/noticias.component';
import { ContactoComponent } from './contacto/contacto.component';
import { TerminosComponent } from './terminos/terminos.component';
import { PrivacidadComponent } from './privacidad/privacidad.component';

export const routes: Routes = [
    { path: 'login', component: LoginComponent },
    { path: 'registro', component: RegistroComponent },
    {
        path: '',
        canActivate: [AuthGuard],
        children: [
            { path: 'home', component: HomeComponent },
            { path: 'analisis', component: AnalisisComponent },
            { path: 'articulos', component: ArticulosComponent },
            { path: 'noticias', component: NoticiasComponent },
            { path: 'videojuego-perfil/:id', component: VideojuegoPerfilComponent },
            { path: 'usuario-perfil/:id', component: UsuarioComponent },
            { path: 'analista-perfil/:id', component: AnalistaComponent },
            { path: 'post/crear', component: PostComponent },
            { path: 'post/:id', component: PostDetailComponent },
            { path: 'post/:id/editar', component: PostComponent },
            { path: 'contacto', component: ContactoComponent },
            { path: 'terminos', component: TerminosComponent },
            { path: 'privacidad', component: PrivacidadComponent },
            { path: '', redirectTo: 'home', pathMatch: 'full' },
        ]
    },
    { path: '**', redirectTo: 'login' }
];
