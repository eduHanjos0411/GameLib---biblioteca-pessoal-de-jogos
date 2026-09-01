import type { ButtonHTMLAttributes } from 'react';

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  variant?: 'primary' | 'secondary' | 'danger';
  isLoading?: boolean;
}

export function Button({ 
  children, 
  variant = 'primary', 
  isLoading, 
  className = '', 
  disabled, 
  ...props 
}: ButtonProps) {
  const baseStyles = "w-full py-2.5 px-4 rounded-lg font-semibold text-sm transition-all duration-200 flex items-center justify-center gap-2 disabled:opacity-50 disabled:cursor-not-allowed";
  
  const variants = {
    primary: "bg-brand-purple hover:bg-opacity-90 text-white shadow-neon-purple hover:shadow-neon-glow border border-brand-neon/30 active:scale-[0.98]",
    secondary: "bg-surface hover:bg-surface-hover text-gray-200 border border-surface-border active:scale-[0.98]",
    danger: "bg-brand-red/10 border border-brand-red/30 text-brand-red hover:bg-brand-red hover:text-white active:scale-[0.98]"
  };

  return (
    <button
      className={`${baseStyles} ${variants[variant]} ${className}`}
      disabled={disabled || isLoading}
      {...props}
    >
      {isLoading ? (
        <span className="w-4 h-4 border-2 border-white/20 border-t-white rounded-full animate-spin" />
      ) : (
        children
      )}
    </button>
  );
}