import { useEffect, useState } from 'react';
import { 
  Gamepad2, 
  Plus, 
  LogOut, 
  Star, 
  Trash2, 
  LayoutGrid,
  SearchX
} from 'lucide-react';
import { Button } from '../components/ui/Button';
import { authService } from '../service/auth';
import { gamesService, type JogoColecao } from '../service/games';
import { AddGameModal } from '../components/AddGameModal';

export function Dashboard() {
  const [jogos, setJogos] = useState<JogoColecao[]>([]);
  const [filterStatus, setFilterStatus] = useState<string>('TODOS');
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

  const jogosFiltrados = filterStatus === 'TODOS'
    ? jogos
    : jogos.filter(j => j.status === filterStatus);

  return (
    <div className="min-h-screen flex flex-col">
      {/* NAVBAR */}
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
                onClick={() => { authService.logout(); window.location.href = '/auth'; }}
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
        {/* FILTROS E TÍTULO */}
        <div className="flex flex-col md:flex-row md:items-center justify-between gap-6 mb-10">
          <div>
            <h1 className="text-3xl font-bold text-white flex items-center gap-3">
              <LayoutGrid className="w-8 h-8 text-brand-neon" />
              Minha Coleção
            </h1>
            <p className="text-gray-400 text-sm mt-1">Gerencie seus jogos, notas e progresso.</p>
          </div>

          <div className="flex items-center gap-2 overflow-x-auto pb-2 md:pb-0 scrollbar-hide">
            {['TODOS', 'JOGANDO', 'ZERADO', 'PLATINADO', 'DESEJADO', 'ABANDONADO'].map((status) => (
              <button
                key={status}
                onClick={() => setFilterStatus(status)}
                className={`px-4 py-2 rounded-full text-xs font-bold whitespace-nowrap transition-all duration-200 border ${
                  filterStatus === status 
                  ? 'bg-brand-purple border-brand-neon text-white shadow-neon-purple' 
                  : 'bg-surface border-surface-border text-gray-400 hover:border-gray-500'
                }`}
              >
                {status}
              </button>
            ))}
          </div>
        </div>

        {/* FEED DE JOGOS */}
        {loading ? (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
            {[1,2,3,4].map(n => (
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
            {jogosFiltrados.map((jogo) => (
              <div 
                key={jogo.id} 
                className="group relative bg-surface border border-surface-border rounded-2xl overflow-hidden hover:border-brand-neon/50 transition-all duration-300 hover:shadow-neon-purple/20"
              >
                {/* Capa do Jogo */}
                <div className="relative h-56 overflow-hidden">
                  <img 
                    src={jogo.capaUrl} 
                    alt={jogo.titulo} 
                    className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110"
                  />
                  <div className="absolute inset-0 bg-linear-to-t from-surface to-transparent opacity-60" />
                  
                  <div className="absolute top-3 left-3">
                    <span className={`px-2 py-1 rounded text-[10px] font-black uppercase tracking-widest border backdrop-blur-md status-tag-${jogo.status}`}>
                      {jogo.status}
                    </span>
                  </div>

                  <button 
                    onClick={() => handleRemoverJogo(jogo.id)}
                    className="absolute top-3 right-3 p-2 bg-black/40 backdrop-blur-md text-gray-300 hover:text-brand-red rounded-lg opacity-0 group-hover:opacity-100 transition-opacity"
                  >
                    <Trash2 className="w-4 h-4" />
                  </button>
                </div>

                {/* Info do Jogo */}
                <div className="p-5">
                  <div className="flex justify-between items-start mb-2">
                    <h3 className="font-bold text-white leading-tight line-clamp-1">{jogo.titulo}</h3>
                    {jogo.nota && (
                      <div className="flex items-center gap-1 text-brand-neon">
                        <Star className="w-3 h-3 fill-brand-neon" />
                        <span className="text-xs font-bold">{jogo.nota}</span>
                      </div>
                    )}
                  </div>
                  
                  <p className="text-[10px] text-brand-neon font-bold uppercase tracking-widest mb-3">
                    {jogo.plataforma}
                  </p>

                  {jogo.comentario && (
                    <p className="text-sm text-gray-400 line-clamp-2 italic mb-4">
                      "{jogo.comentario}"
                    </p>
                  )}

                  <div className="pt-4 border-t border-surface-border">
                    <button className="text-[11px] font-bold text-gray-400 hover:text-white transition-colors uppercase tracking-wider">
                      Ver detalhes
                    </button>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </main>

      <AddGameModal 
        isOpen={isModalOpen} 
        onClose={() => setIsModalOpen(false)} 
        onGameAdded={carregarBiblioteca} 
      />

      {/* Botão Flutuante Mobile */}
      <button 
        onClick={() => setIsModalOpen(true)}
        className="fixed bottom-6 right-6 w-14 h-14 bg-brand-purple rounded-full flex items-center justify-center text-white shadow-neon-glow sm:hidden z-50 active:scale-95 transition-transform"
      >
        <Plus className="w-8 h-8" />
      </button>
    </div>
  );
}