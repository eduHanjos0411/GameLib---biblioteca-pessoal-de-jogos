import { forwardRef } from 'react';
import type { InputHTMLAttributes } from 'react';


interface InputProps extends InputHTMLAttributes<HTMLInputElement> {
  label?: string;
  error?: string;
}

export const Input = forwardRef<HTMLInputElement, InputProps>(
  ({ label, error, className = '', ...props }, ref) => {
    return (
      <div className="w-full space-y-1.5">
        {label && (
          <label className="text-xs uppercase tracking-wider font-semibold text-gray-400">
            {label}
          </label>
        )}
        <input
          ref={ref}
          className={`w-full bg-surface border border-surface-border rounded-lg px-4 py-2.5 text-sm text-gray-100 placeholder-gray-500 focus:outline-none focus:border-brand-neon focus:ring-1 focus:ring-brand-neon transition-all duration-200 ${
            error ? 'border-brand-red' : ''
          } ${className}`}
          {...props}
        />
        {error && <span className="text-xs text-brand-red font-medium">{error}</span>}
      </div>
    );
  }
);