import { Multimedia } from "./Multimedia";

// src/app/models/post.model.ts
export type PostTipo = 'analisis' | 'articulo' | 'noticia';

export interface Post {
  id?: number;                  // opcional para creación
  titulo: string;
  subtitulo?: string;
  contenido: string;
  descripcion: string;
  fechaPublicacion: string;     // ISO string, p. ej. "2025-05-14"
  tipo: PostTipo;
  id_usuario: number;
  miniatura?: Multimedia;           
  portada?: Multimedia;             
  imagenContenido1?: Multimedia;
  imagenContenido2?: Multimedia;
  imagenContenido3?: Multimedia;
  usuarioId: number; 
}
