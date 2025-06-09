import { Multimedia } from "./Multimedia";
import { PostMultimedia } from "./PostMultimedia";

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
  usuarioId: number;

  // Imágenes principales
  miniatura?: Multimedia;
  portada?: Multimedia;
  imagenContenido1?: Multimedia;
  imagenContenido2?: Multimedia;
  imagenContenido3?: Multimedia;

  // Arrays de imágenes y multimedia

imagenes?: PostMultimedia[]; // 
  multimedia?: Multimedia[];
  multimediaIds?: number[];

  // IDs de imágenes principales
  miniaturaId?: number;
  portadaId?: number;
  imagenContenido1Id?: number;
  imagenContenido2Id?: number;
  imagenContenido3Id?: number;

  // Otros campos opcionales según tu backend
  comentarios?: any[];
  reacciones?: any[];
  etiquetas?: any[];
  categorias?: any[];
  postMultimedia?: any[];
  nota?: any;
}
