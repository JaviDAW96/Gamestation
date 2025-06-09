import { Multimedia } from "./Multimedia";

export interface PostMultimedia {
  postId: number;
  multimediaId: number;
  multimedia: Multimedia;
  rol: string;
}