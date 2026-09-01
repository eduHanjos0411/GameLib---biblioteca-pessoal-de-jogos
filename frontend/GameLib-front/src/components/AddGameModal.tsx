import { useState, FormEvent } from 'react';
import { 
  X, 
  Search, 
  ChevronLeft, 
  Gamepad2, 
  CheckCircle2, 
  Loader2,
  Info
} from 'lucide-react';
import { Button } from './ui/Button';
import { Input } from './ui/Input';
import { gamesService, type JogoExternoDTO, type StatusJogo } from '../service/games';

interface AddGameModalProps {
  isOpen: boolean;
  onClose: () => void;
  onGameAdded: () => void;
}

export function AddGameModal({ isOpen, onClose, onGameAdded }: AddGameModalProps) {
  const [query, setQuery] = useState('');
  const [searchResults, setSearchResults] = useState<JogoExternoDTO[]>([]);
  const [selectedGame, setSelectedGame] = useState<JogoExternoDTO | null>(null);
  
  const [plataforma, setPlataforma] = useState('');
  const [status, setStatus] = useState<StatusJogo>('JOGANDO');
  const [nota, setNota] = useState<number>(8);
  const [comentario, setComentario] = useState('');
  
  const [searching, setSearching] = useState(false);
  const [saving, setSaving] = useState(false);

  if (!isOpen) return null;

  const handleSearch = async (e: FormEvent) => {
    e.preventDefault();
    if (!query.trim()) return;

    setSearching(true);
    try {
      const results = await gamesService.buscarJogosExternos(query);
      setSearchResults(results);
    } catch (err) {
      console.error('Erro ao buscar jogo:', err);
    } finally {
      setSearching(false);
    }
  };

  const handleSelectGame = (game: JogoExternoDTO) => {
    setSelectedGame(game);
    if (game.plataformas && game.plataformas.length > 0) {
      setPlataforma(game.plataformas[0]);
    }
  };

  const handleSave = async (e: FormEvent) => {
    e.preventDefault();
    if (!selectedGame) return;

    setSaving(true);
    try {
      await gamesService.adicionarJogo({
        titulo: selectedGame.nome,
        capaUrl: selectedGame.capaUrl,
        rawgId: selectedGame.rawgId,
        plataforma,
        status,
        nota: Number(nota),
        comentario
      });

      setSelectedGame(null);
      setSearchResults([]);
      setQuery('');
      onGameAdded();
      onClose();
    } catch (err) {
      console.error('Erro ao salvar jogo:', err);
    } finally {
      setSaving(false);
    }
  };

  const handleBack = () => {
    setSelectedGame(null);
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/80 backdrop-blur-sm animate-in fade-in duration-300">
      <div className="relative w-full max-w-2xl bg-surface border border-surface-border rounded-2xl shadow-2xl shadow-black overflow-hidden flex flex-col max-h-[90vh]">
        
        {/* Glow Decorativo */}
        <div className="absolute top-0 left-1/2 -translate-x-1/2 w-1/2 h-1 bg-linear-to-r from-transparent via-brand-neon to-transparent opacity-50" />

        {/* HEADER */}
        <div className="flex items-center justify-between p-6 border-b border-surface-border bg-surface-hover/30">
          <div className="flex items-center gap-3">
            {selectedGame ? (
              <button 
                onClick={handleBack}
                className="p-1 hover:bg-surface-border rounded-lg text-gray-400 hover:text-white transition-colors"
              >
                <ChevronLeft className="w-6 h-6" />
              </button>
            ) : (
              <div className="p-2 bg-brand-purple/20 rounded-lg text-brand-neon">
                <Gamepad2 className="w-5 h-5" />
              </div>
            )}
            <h2 className="text-xl font-bold text-white tracking-tight">
              {selectedGame ? 'Configurar Jogo' : 'Adicionar Jogo'}
            </h2>
          </div>
          <button 
            onClick={onClose}
            className="p-2 text-gray-500 hover:text-white hover:bg-surface-border rounded-xl transition-all"
          >
            <X className="w-5 h-5" />
          </button>
        </div>

        <div className="overflow-y-auto flex-1 p-6 custom-scrollbar">
          {!selectedGame ? (
            /* ETAPA 1: BUSCA */
            <div className="space-y-6">
              <form onSubmit={handleSearch} className="relative group">
                <Input
                  placeholder="Pesquisar por nome do jogo..."
                  value={query}
                  onChange={(e) => setQuery(e.target.value)}
                  className="pl-12 h-14 text-base"
                  autoFocus
                />
                <Search className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-gray-500 group-focus-within:text-brand-neon transition-colors" />
                <div className="absolute right-3 top-1/2 -translate-y-1/2">
                   <Button variant="primary" className="h-9 px-4 text-xs" isLoading={searching}>
                      Buscar
                   </Button>
                </div>
              </form>

              <div className="grid grid-cols-1 gap-3">
                {searching ? (
                  <div className="flex flex-col items-center justify-center py-12 text-gray-500">
                    <Loader2 className="w-8 h-8 animate-spin mb-2 text-brand-neon" />
                    <p className="text-sm font-medium animate-pulse">Consultando banco de dados da RAWG...</p>
                  </div>
                ) : searchResults.length > 0 ? (
                  searchResults.map((game, index) => (
                    <button
                      key={game.rawgId ? `rawg-${game.rawgId}` : `idx-${index}`}
                      onClick={() => handleSelectGame(game)}
                      className="group flex items-center gap-4 p-3 bg-surface-hover/40 border border-surface-border rounded-xl hover:border-brand-neon/50 hover:bg-surface-hover transition-all text-left"
                    >
                      <div className="w-16 h-16 rounded-lg overflow-hidden shrink-0 border border-surface-border">
                        <img src={game.capaUrl} alt="" className="w-full h-full object-cover" />
                      </div>
                      <div className="flex-1 min-w-0">
                        <h4 className="text-sm font-bold text-white truncate group-hover:text-brand-neon transition-colors">
                          {game.nome}
                        </h4>
                        <p className="text-xs text-gray-500 mt-0.5 uppercase tracking-tighter">
                          {game.generos?.slice(0, 3).join(' • ')}
                        </p>
                      </div>
                      <PlusIcon />
                    </button>
                  ))
                ) : query && !searching && (
                  <div className="text-center py-12 border-2 border-dashed border-surface-border rounded-2xl">
                    <Info className="w-8 h-8 text-gray-600 mx-auto mb-2" />
                    <p className="text-gray-500 text-sm">Nenhum resultado para "{query}"</p>
                  </div>
                )}
              </div>
            </div>
          ) : (
            /* ETAPA 2: CONFIGURAÇÃO */
            <form onSubmit={handleSave} className="space-y-6">
              <div className="relative h-40 rounded-2xl overflow-hidden border border-surface-border group">
                <img 
                  src={selectedGame.capaUrl} 
                  className="w-full h-full object-cover transition-transform duration-700 group-hover:scale-110" 
                  alt="" 
                />
                <div className="absolute inset-0 bg-linear-to-t from-background via-background/40 to-transparent" />
                <div className="absolute bottom-4 left-4 right-4">
                  <h3 className="text-xl font-black text-white uppercase italic tracking-tighter drop-shadow-lg">
                    {selectedGame.nome}
                  </h3>
                </div>
              </div>

              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="space-y-1.5">
                  <label className="text-[10px] uppercase font-bold text-gray-500 tracking-widest ml-1">
                    Plataforma
                  </label>
                  <Input
                    value={plataforma}
                    onChange={(e) => setPlataforma(e.target.value)}
                    placeholder="Ex: PC, PS5, Switch"
                    required
                  />
                </div>

                <div className="space-y-1.5">
                  <label className="text-[10px] uppercase font-bold text-gray-500 tracking-widest ml-1">
                    Status Atual
                  </label>
                  <select 
                    value={status} 
                    onChange={(e) => setStatus(e.target.value as StatusJogo)}
                    className="w-full bg-surface border border-surface-border rounded-lg px-4 py-2.5 text-sm text-gray-100 focus:outline-none focus:border-brand-neon focus:ring-1 focus:ring-brand-neon transition-all"
                  >
                    <option value="JOGANDO">🕹️ Jogando</option>
                    <option value="ZERADO">🏆 Zerado</option>
                    <option value="PLATINADO">👑 Platinado</option>
                    <option value="DESEJADO">💖 Desejado</option>
                    <option value="ABANDONADO">💀 Abandonado</option>
                  </select>
                </div>
              </div>

              <div className="space-y-2">
                <div className="flex justify-between items-end px-1">
                  <label className="text-[10px] uppercase font-bold text-gray-500 tracking-widest">
                    Sua Nota
                  </label>
                  <span className="text-lg font-black text-brand-neon">{nota}/10</span>
                </div>
                <input
                  type="range"
                  min="0"
                  max="10"
                  step="0.5"
                  value={nota}
                  onChange={(e) => setNota(Number(e.target.value))}
                  className="w-full h-2 bg-surface-border rounded-lg appearance-none cursor-pointer accent-brand-purple"
                />
              </div>

              <div className="space-y-1.5">
                <label className="text-[10px] uppercase font-bold text-gray-500 tracking-widest ml-1">
                  Análise rápida
                </label>
                <textarea
                  value={comentario}
                  onChange={(e) => setComentario(e.target.value)}
                  placeholder="O que você achou deste jogo?"
                  className="w-full min-h-25 bg-surface border border-surface-border rounded-xl p-4 text-sm text-gray-100 placeholder-gray-600 focus:outline-none focus:border-brand-neon focus:ring-1 focus:ring-brand-neon transition-all resize-none"
                />
              </div>

              <Button type="submit" isLoading={saving} className="h-12 text-base">
                <CheckCircle2 className="w-5 h-5" /> Confirmar Adição
              </Button>
            </form>
          )}
        </div>
      </div>
    </div>
  );
}

function PlusIcon() {
  return (
    <div className="w-8 h-8 rounded-full border border-surface-border flex items-center justify-center text-gray-500 group-hover:bg-brand-neon group-hover:text-background group-hover:border-brand-neon transition-all">
      <Search className="w-4 h-4" />
    </div>
  );
}