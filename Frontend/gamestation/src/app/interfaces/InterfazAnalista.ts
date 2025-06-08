import { Post } from "./Post";
import { InterfazUsuario } from "./InterfazUsuario";

export interface InterfazAnalista {
  id: number;
  descripcion: string;
  experienciaLaboral: string;
  noticiasPublicadas: number;
  id_usuario: number;
  posts?: Post[];
  usuario: InterfazUsuario;
}