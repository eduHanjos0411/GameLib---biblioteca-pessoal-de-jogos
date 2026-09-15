import { api } from "./api";

export type StatusJogo =
  | "JOGANDO"
  | "FINALIZADO"
  | "ABANDONADO"
  | "NAO_INICIADO";

export interface JogoColecao {
  id?: number;
  idJogo?: number;
  titulo: string;
  background_image?: string;
  urlCapa?: string;
  plataforma: string;
  status?: StatusJogo;
  statusJogo?: StatusJogo;
  nota?: number;
  comentario?: string;
  opiniao?: string;
}

export interface JogoExternoDTO {
  id: number;
  name: string;
  background_image: string;
  genres?: string[];
}

export interface CriarJogoDTO {
  titulo: string;
  background_image?: string;
  plataforma: string;
  status: StatusJogo;
  nota?: number;
  comentario?: string;
  id?: number;
}

export interface AtualizarJogoDTO {
  plataforma?: string;
  status?: StatusJogo;
  statusJogo?: StatusJogo;
  nota?: number;
  comentario?: string;
  opiniao?: string;
}

const normalizeJogoColecao = (jogo: any): JogoColecao => ({
  ...jogo,
  background_image: jogo.background_image ?? jogo.urlCapa ?? "",
  status: jogo.status ?? jogo.statusJogo ?? "NAO_INICIADO",
  statusJogo: jogo.statusJogo ?? jogo.status ?? "NAO_INICIADO",
  comentario: jogo.comentario ?? jogo.opiniao ?? "",
});

export const gamesService = {
  async listarMeusJogos(): Promise<JogoColecao[]> {
    const { data } = await api.get<any[]>("/biblioteca");
    return data.map(normalizeJogoColecao);
  },

  async buscarJogosExternos(query: string): Promise<JogoExternoDTO[]> {
    const { data } = await api.get<JogoExternoDTO[]>(`/jogos-externos/buscar`, {
      params: { nome: query },
    });
    return data;
  },

  async adicionarJogo(jogo: CriarJogoDTO): Promise<JogoColecao> {
    const { data } = await api.post<any>("/biblioteca", jogo);
    return normalizeJogoColecao(data);
  },

  async atualizarJogo(id: number, dados: AtualizarJogoDTO): Promise<JogoColecao> {
    const payload = {
      plataforma: dados.plataforma,
      statusJogo: dados.statusJogo ?? dados.status,
      nota: dados.nota,
      opiniao: dados.comentario ?? dados.opiniao,
    };

    const { data } = await api.put<any>(`/biblioteca/${id}`, payload);
    return normalizeJogoColecao(data);
  },

  async removerJogo(id: number): Promise<void> {
    await api.delete(`/biblioteca/${id}`);
  },
};
