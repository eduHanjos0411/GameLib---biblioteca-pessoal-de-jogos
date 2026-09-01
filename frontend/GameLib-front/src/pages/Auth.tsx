import { useState, FormEvent } from 'react';
import { useNavigate } from 'react-router-dom';
import { Gamepad2, LogIn, UserPlus, AlertCircle } from 'lucide-react';
import { Input } from '../components/ui/Input';
import { Button } from '../components/ui/Button';
import { authService } from '../service/auth';

export function Auth() {
  const navigate = useNavigate();
  const [isLogin, setIsLogin] = useState(true);
  
  const [nome, setNome] = useState('');
  const [email, setEmail] = useState('');
  const [senha, setSenha] = useState('');
  
  const [loading, setLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState('');

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setErrorMsg('');

    try {
      if (isLogin) {
        await authService.login({ email, senha });
      } else {
        await authService.cadastrar({ nome, email, senha });
        await authService.login({ email, senha });
      }
      navigate('/dashboard');
    } catch (err: any) {
      const message = err.response?.data?.message || 'Falha na autenticação. Verifique os dados.';
      setErrorMsg(message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen w-full flex items-center justify-center p-4">
      <div className="w-full max-w-md bg-surface/80 backdrop-blur-xl border border-surface-border rounded-2xl p-8 shadow-2xl shadow-black/80 relative overflow-hidden">
        
        {/* Glow Decorativo de Fundo */}
        <div className="absolute -top-24 -left-24 w-48 h-48 bg-brand-purple/30 rounded-full blur-3xl pointer-events-none" />
        <div className="absolute -bottom-24 -right-24 w-48 h-48 bg-brand-pink/20 rounded-full blur-3xl pointer-events-none" />

        {/* Header da Card */}
        <div className="flex flex-col items-center mb-8 text-center relative z-10">
          <div className="w-14 h-14 bg-brand-purple/20 border border-brand-neon/40 rounded-2xl flex items-center justify-center mb-3 text-brand-neon shadow-neon-glow">
            <Gamepad2 className="w-8 h-8" />
          </div>
          <h1 className="text-2xl font-bold tracking-tight text-white">
            GAMELIB<span className="text-brand-neon">.</span>
          </h1>
          <p className="text-xs text-gray-400 mt-1">
            {isLogin ? 'Entre para gerenciar sua biblioteca gamer' : 'Crie sua conta e comece sua coleção'}
          </p>
        </div>

        {/* Alerta de Erro */}
        {errorMsg && (
          <div className="mb-6 p-3 bg-brand-red/10 border border-brand-red/30 rounded-lg flex items-center gap-2 text-xs text-brand-red">
            <AlertCircle className="w-4 h-4 shrink-0" />
            <span>{errorMsg}</span>
          </div>
        )}

        {/* Formulário */}
        <form onSubmit={handleSubmit} className="space-y-4 relative z-10">
          {!isLogin && (
            <Input
              label="Nome do Gamer"
              placeholder="Ex: Henrique"
              value={nome}
              onChange={(e) => setNome(e.target.value)}
              required
            />
          )}

          <Input
            label="E-mail"
            type="email"
            placeholder="seu@email.com"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />

          <Input
            label="Senha"
            type="password"
            placeholder="••••••••"
            value={senha}
            onChange={(e) => setSenha(e.target.value)}
            required
          />

          <Button type="submit" isLoading={loading} className="mt-2">
            {isLogin ? (
              <>
                <LogIn className="w-4 h-4" /> Entrar
              </>
            ) : (
              <>
                <UserPlus className="w-4 h-4" /> Cadastrar
              </>
            )}
          </Button>
        </form>

        {/* Toggle Login/Cadastro */}
        <div className="mt-6 pt-6 border-t border-surface-border text-center relative z-10">
          <p className="text-xs text-gray-400">
            {isLogin ? 'Ainda não tem uma conta?' : 'Já possui uma conta?'}
            <button
              type="button"
              onClick={() => {
                setIsLogin(!isLogin);
                setErrorMsg('');
              }}
              className="ml-2 font-semibold text-brand-neon hover:underline focus:outline-none"
            >
              {isLogin ? 'Criar Conta' : 'Fazer Login'}
            </button>
          </p>
        </div>

      </div>
    </div>
  );
}