import { api } from './api';

export interface LoginDTO {
  email: string;
  senha: string;
}

export interface CadastroDTO {
  nome: string;
  email: string;
  senha: string;
}

export interface TokenResponse {
  token: string;
  type: string;
  usuarioId: number;
  nome: string;
}

export const authService = {
  async login(credentials: LoginDTO): Promise<TokenResponse> {
    const { data } = await api.post<TokenResponse>('/usuarios/login', credentials);
    if (data.token) {
      localStorage.setItem('@GameLib:token', data.token);
      localStorage.setItem('@GameLib:user', JSON.stringify({ id: data.usuarioId, nome: data.nome }));
    }
    return data;
  },

  async cadastrar(credentials: CadastroDTO) {
    const { data } = await api.post('/usuarios/cadastrar', credentials);
    return data;
  },

  logout() {
    localStorage.removeItem('@GameLib:token');
    localStorage.removeItem('@GameLib:user');
  },

  getUsuarioLogado() {
    const userString = localStorage.getItem('@GameLib:user');
    return userString ? JSON.parse(userString) : null;
  },

  isAuthenticated(): boolean {
    return !!localStorage.getItem('@GameLib:token');
  }
};