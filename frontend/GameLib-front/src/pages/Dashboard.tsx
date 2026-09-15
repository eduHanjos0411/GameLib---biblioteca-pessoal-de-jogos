import { useEffect, useState } from 'react';
import {
  Gamepad2,
  Plus,
  LogOut,
  Star,
  Trash2,
  LayoutGrid,
  SearchX,
} from 'lucide-react';
import { Button } from '../components/ui/Button';
import { authService } from '../service/auth';
import { gamesService, type JogoColecao, type StatusJogo } from '../service/games';
import { AddGameModal } from '../components/AddGameModal';

const statusLabels: Record<string, string> = {
  TODOS: 'TODOS',
  JOGANDO: 'JOGANDO',
  FINALIZADO: 'FINALIZADO',
  ABANDONADO: 'ABANDONADO',
  NAO_INICIADO: 'NÃO INICIADO',
};

export function Dashboard() {
  const [jogos, setJogos] = useState<JogoColecao[]>([]);
  const [filterStatus, setFilterStatus] = useState<string>('TODOS');
  const [expandedId, setExpandedId] = useState<number | null>(null);
  const [editingId, setEditingId] = useState<number | null>(null);
  const [editForm, setEditForm] = useState<{ plataforma: string; status: StatusJogo; nota: number; comentario: string } | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [loading, setLoading] = useState(true);

  const usuario = authService.getUsuarioLogado();

  const carregarBiblioteca = async () => {
    setLoading(true);
    try {
      const data = await gamesService.listarMeusJogos();
      setJogos(data);
    } catch (err) {
      console.error('Erro ao carregar jogos:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    carregarBiblioteca();
  }, []);

  const handleRemoverJogo = async (id: number) => {
    if (confirm('Deseja remover este jogo da sua coleção?')) {
      await gamesService.removerJogo(id);
      carregarBiblioteca();
    }
  };

  const abrirEdicao = (jogo: JogoColecao) => {
    setEditingId(jogo.id ?? null);
    setEditForm({
      plataforma: jogo.plataforma ?? '',
      status: (jogo.status ?? jogo.statusJogo ?? 'NAO_INICIADO') as StatusJogo,
      nota: jogo.nota ?? 0,
      comentario: jogo.comentario ?? '',
    });
  };

  const salvarEdicao = async (id: number) => {
    if (!editForm) return;

    await gamesService.atualizarJogo(id, {
      plataforma: editForm.plataforma,
      status: editForm.status,
      nota: editForm.nota,
      comentario: editForm.comentario,
    });

    setEditingId(null);
    setEditForm(null);
    carregarBiblioteca();
  };

  const statusOptions = ['TODOS', 'JOGANDO', 'FINALIZADO', 'ABANDONADO', 'NAO_INICIADO'];

  const jogosFiltrados =
    filterStatus === 'TODOS'
      ? jogos
      : jogos.filter((j) => (j.status ?? j.statusJogo ?? 'NAO_INICIADO') === filterStatus);

  return (
    <div className="min-h-screen flex flex-col">
      <header className="sticky top-0 z-40 w-full bg-surface/60 backdrop-blur-md border-b border-surface-border">
        <div className="max-w-7xl mx-auto px-4 h-16 flex items-center justify-between">
          <div className="flex items-center gap-2">
            <div className="w-8 h-8 bg-brand-purple rounded-lg flex items-center justify-center shadow-neon-purple">
              <Gamepad2 className="w-5 h-5 text-white" />
            </div>
            <span className="font-bold text-xl tracking-tight">
              GAMELIB<span className="text-brand-neon">.</span>
            </span>
          </div>

          <div className="flex items-center gap-4">
            <Button
              variant="primary"
              className="w-auto h-9 px-4 hidden sm:flex"
              onClick={() => setIsModalOpen(true)}
            >
              <Plus className="w-4 h-4" /> Adicionar Jogo
            </Button>

            <div className="h-8 w-px bg-surface-border mx-2" />

            <div className="flex items-center gap-3">
              <div className="text-right hidden md:block">
                <p className="text-sm font-semibold text-white leading-none">{usuario?.nome}</p>
                <p className="text-[10px] text-gray-400 uppercase tracking-widest mt-1">Gamer</p>
              </div>
              <button
                onClick={() => {
                  authService.logout();
                  window.location.href = '/auth';
                }}
                className="p-2 text-gray-400 hover:text-brand-red hover:bg-brand-red/10 rounded-lg transition-colors"
                title="Sair"
              >
                <LogOut className="w-5 h-5" />
              </button>
            </div>
          </div>
        </div>
      </header>

      <main className="flex-1 max-w-7xl w-full mx-auto px-4 py-8">
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-6 mb-10">
          <div>
            <h1 className="text-3xl font-bold text-white flex items-center gap-3">
              <LayoutGrid className="w-8 h-8 text-brand-neon" />
              Minha Coleção
            </h1>
            <p className="text-gray-400 text-sm mt-1">Gerencie seus jogos, notas e progresso.</p>
          </div>

          <div className="flex items-center gap-2 overflow-x-auto pb-2 md:pb-0 scrollbar-hide">
            {statusOptions.map((status) => (
              <button
                key={status}
                onClick={() => setFilterStatus(status)}
                className={`px-4 py-2 rounded-full text-xs font-bold whitespace-nowrap transition-all duration-200 border ${
                  filterStatus === status
                    ? 'bg-brand-purple border-brand-neon text-white shadow-neon-purple'
                    : 'bg-surface border-surface-border text-gray-400 hover:border-gray-500'
                }`}
              >
                {statusLabels[status] ?? status}
              </button>
            ))}
          </div>
        </div>

        {loading ? (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {[1, 2, 3, 4].map((n) => (
              <div key={n} className="h-100 bg-surface/40 animate-pulse rounded-2xl border border-surface-border" />
            ))}
          </div>
        ) : jogosFiltrados.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-20 text-center">
            <div className="w-20 h-20 bg-surface rounded-full flex items-center justify-center mb-4 border border-surface-border">
              <SearchX className="w-10 h-10 text-gray-500" />
            </div>
            <h3 className="text-xl font-semibold text-white">Nenhum jogo encontrado</h3>
            <p className="text-gray-400 max-w-xs mt-2">Você ainda não adicionou jogos com este status à sua biblioteca.</p>
            <Button className="mt-6 w-auto px-8" onClick={() => setIsModalOpen(true)}>
              Começar Coleção
            </Button>
          </div>
        ) : (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {jogosFiltrados.map((jogo) => {
              const statusValue = (jogo.status ?? jogo.statusJogo ?? 'NAO_INICIADO') as string;
              const imageUrl = jogo.background_image || jogo.urlCapa || 'https://placehold.co/600x400/1f2937/ffffff?text=GameLib';

              return (
                <div
                  key={jogo.id}
                  className="group relative bg-surface border border-surface-border rounded-2xl overflow-hidden hover:border-brand-neon/50 transition-all duration-300 hover:shadow-neon-purple/20"
                >
                  <div className="relative h-56 overflow-hidden">
                    <img
                      src={imageUrl}
                      alt={jogo.titulo}
                      className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110"
                    />
                    <div className="absolute inset-0 bg-gradient-to-t from-surface to-transparent opacity-60" />

                    <div className="absolute top-3 left-3">
                      <span className="px-2 py-1 rounded text-[10px] font-black uppercase tracking-widest border backdrop-blur-md">
                        {statusLabels[statusValue] ?? statusValue}
                      </span>
                    </div>

                    <button
                      onClick={() => {
                        if (jogo.id !== undefined) {
                          handleRemoverJogo(jogo.id);
                        }
                      }}
                      className="absolute top-3 right-3 p-2 bg-black/40 backdrop-blur-md text-gray-300 hover:text-brand-red rounded-lg opacity-0 group-hover:opacity-100 transition-opacity"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                  </div>

                  <div className="p-5">
                    <div className="flex justify-between items-start gap-2 mb-2">
                      <h3 className="font-bold text-white leading-tight break-words">{jogo.titulo}</h3>
                      {jogo.nota !== undefined && jogo.nota !== null && (
                        <div className="flex items-center gap-1 text-brand-neon shrink-0">
                          <Star className="w-3 h-3 fill-brand-neon" />
                          <span className="text-xs font-bold">{jogo.nota}</span>
                        </div>
                      )}
                    </div>

                    <p className="text-[10px] text-brand-neon font-bold uppercase tracking-widest mb-3">
                      {jogo.plataforma}
                    </p>

                    {expandedId === jogo.id && !editingId && (
                      <div className="mb-4 rounded-xl border border-surface-border bg-surface-hover/50 p-3 text-sm text-gray-300">
                        {jogo.comentario || 'Nenhuma observação registrada para este jogo.'}
                      </div>
                    )}

                    {editingId === jogo.id && editForm && (
                      <div className="mb-4 space-y-3 rounded-xl border border-brand-neon/40 bg-surface-hover/60 p-3">
                        <input
                          value={editForm.plataforma}
                          onChange={(e) => setEditForm((prev) => prev ? { ...prev, plataforma: e.target.value } : prev)}
                          className="w-full rounded-lg border border-surface-border bg-surface px-3 py-2 text-sm text-white outline-none focus:border-brand-neon"
                          placeholder="Plataforma"
                        />

                        <select
                          value={editForm.status}
                          onChange={(e) => setEditForm((prev) => prev ? { ...prev, status: e.target.value as StatusJogo } : prev)}
                          className="w-full rounded-lg border border-surface-border bg-surface px-3 py-2 text-sm text-white outline-none focus:border-brand-neon"
                        >
                          <option value="JOGANDO">JOGANDO</option>
                          <option value="FINALIZADO">FINALIZADO</option>
                          <option value="ABANDONADO">ABANDONADO</option>
                          <option value="NAO_INICIADO">NAO_INICIADO</option>
                        </select>

                        <input
                          type="range"
                          min="0"
                          max="10"
                          step="0.5"
                          value={editForm.nota}
                          onChange={(e) => setEditForm((prev) => prev ? { ...prev, nota: Number(e.target.value) } : prev)}
                          className="w-full accent-brand-purple"
                        />

                        <textarea
                          value={editForm.comentario}
                          onChange={(e) => setEditForm((prev) => prev ? { ...prev, comentario: e.target.value } : prev)}
                          rows={3}
                          className="w-full rounded-lg border border-surface-border bg-surface px-3 py-2 text-sm text-white outline-none focus:border-brand-neon"
                          placeholder="Comentário"
                        />

                        <div className="flex gap-2">
                          <button
                            type="button"
                            onClick={() => jogo.id !== undefined && salvarEdicao(jogo.id)}
                            className="flex-1 rounded-lg bg-brand-purple px-3 py-2 text-xs font-bold text-white"
                          >
                            Salvar
                          </button>
                          <button
                            type="button"
                            onClick={() => {
                              setEditingId(null);
                              setEditForm(null);
                            }}
                            className="flex-1 rounded-lg border border-surface-border px-3 py-2 text-xs font-bold text-gray-300"
                          >
                            Cancelar
                          </button>
                        </div>
                      </div>
                    )}

                    <div className="pt-4 border-t border-surface-border flex gap-2">
                      <button
                        type="button"
                        onClick={() => setExpandedId((current) => (current === jogo.id ? null : jogo.id ?? null))}
                        className="text-[11px] font-bold text-gray-400 hover:text-white transition-colors uppercase tracking-wider"
                      >
                        {expandedId === jogo.id ? 'Fechar detalhes' : 'Ver detalhes'}
                      </button>

                      <button
                        type="button"
                        onClick={() => abrirEdicao(jogo)}
                        className="text-[11px] font-bold text-brand-neon hover:text-white transition-colors uppercase tracking-wider"
                      >
                        Editar
                      </button>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </main>

      <AddGameModal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onGameAdded={carregarBiblioteca}
      />

      <button
        onClick={() => setIsModalOpen(true)}
        className="fixed bottom-6 right-6 w-14 h-14 bg-brand-purple rounded-full flex items-center justify-center text-white shadow-neon-glow sm:hidden z-50 active:scale-95 transition-transform"
      >
        <Plus className="w-8 h-8" />
      </button>
    </div>
  );
}