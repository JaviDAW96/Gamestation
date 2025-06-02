// src/app/models/usuario.model.ts
export interface Usuario {
  id: number;
  nombre: string;
  apellidos: string;
  dni: string;
  email: string;
  fechaNacimiento: string; // ISO (yyyy-MM-dd) o Date
  imagen?: string;          // URL o base64
  password?: string;
}
