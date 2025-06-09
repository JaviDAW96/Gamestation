export interface InterfazUsuario {
  id: number;
  rol: { id: number; rol: string };
  nombre: string;
  apellidos: string;
  dni: string;
  email: string;
  fechaNacimiento: string;
  imagen: string | null;
  password: string;
  
}