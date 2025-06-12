import { Multimedia } from "./Multimedia";
import { PostMultimedia } from "./PostMultimedia";

// src/app/models/post.model.ts
export type PostTipo = 'analisis' | 'articulo' | 'noticia';

export interface Post {
  id?: number;                 
  titulo: string;
  subtitulo?: string;
  contenido: string;
  descripcion: string;
  fechaPublicacion: string;     
  tipo: PostTipo;
  usuarioId: number;


  miniatura?: Multimedia;
  portada?: Multimedia;
  imagenContenido1?: Multimedia;
  imagenContenido2?: Multimedia;
  imagenContenido3?: Multimedia;


imagenes?: PostMultimedia[]; // 
  multimedia?: Multimedia[];
  multimediaIds?: number[];

  miniaturaId?: number;
  portadaId?: number;
  imagenContenido1Id?: number;
  imagenContenido2Id?: number;
  imagenContenido3Id?: number;

  comentarios?: any[];
  reacciones?: any[];
  etiquetas?: any[];
  categorias?: any[];
  postMultimedia?: any[];
  nota?: any;
}
