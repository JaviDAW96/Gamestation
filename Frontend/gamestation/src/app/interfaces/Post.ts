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
  portada?: string;             // <-- Agrega esta línea aquí
}
