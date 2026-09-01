import { api } from "./api";

export type StatusJogo =
  | "JOGANDO"
  | "ZERADO"
  | "PLATINADO"
  | "ABANDONADO"
  | "DESEJADO";

export interface JogoColecao {
  idBase: number;
  name: string;
  background_image: string;
  plataforma: string;
  status: StatusJogo;
  nota?: number;
  comentario?: string;
  id?: number;
}

export interface JogoExternoDTO {
  id: number;
  name: string;
  background_image: string;
  genres?: string[];
  plataformas?: string[];
}

export interface CriarJogoDTO {
  name: string;
  background_image: string;
  plataforma: string;
  status: StatusJogo;
  nota?: number;
  comentario?: string;
  id?: number;
}

export const gamesService = {
  // Busca a coleção do usuário autenticado
  async listarMeusJogos(): Promise<JogoColecao[]> {
    const { data } = await api.get<JogoColecao[]>("/jogos");
    return data;
  },

  // Busca jogos externos via RAWG API
  async buscarJogosExternos(query: string): Promise<JogoExternoDTO[]> {
    const { data } = await api.get<JogoExternoDTO[]>(`/jogos-externos/buscar`, {
      params: {
        nome: query, // Chave obrigatória mapeada no @RequestParam("nome")
      },
    });
    console.log(data)
    return data;
    
  },

  // Adiciona um jogo à biblioteca do usuário
  async adicionarJogo(jogo: CriarJogoDTO): Promise<JogoColecao> {
    const { data } = await api.post<JogoColecao>("/jogos", jogo);
    return data;
  },

  // Remove um jogo da biblioteca
  async removerJogo(id: number): Promise<void> {
    await api.delete(`/jogos/${id}`);
  },
};
