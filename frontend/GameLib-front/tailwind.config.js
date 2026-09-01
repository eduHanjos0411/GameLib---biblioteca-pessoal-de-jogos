/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        background: '#0a0910', // Preto profundo / Ash dark
        surface: {
          DEFAULT: '#13111c',
          hover: '#1c1929',
          border: '#2a243a'
        },
        brand: {
          purple: '#6300b2',
          neon: '#a855f7',
          pink: '#ec4899',
          red: '#ff0000',
        }
      },
      fontFamily: {
        sans: ['Inter', 'sans-serif'],
      },
      boxShadow: {
        'neon-purple': '0 0 20px -3px rgba(99, 0, 178, 0.5)',
        'neon-glow': '0 0 15px rgba(168, 85, 247, 0.4)',
      }
    },
  },
  plugins: [],
}