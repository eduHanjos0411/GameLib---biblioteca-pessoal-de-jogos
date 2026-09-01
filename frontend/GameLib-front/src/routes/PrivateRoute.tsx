import { Navigate } from 'react-router-dom';
import type { JSX } from 'react/jsx-runtime';
import { authService } from '../service/auth';

export function PrivateRoute({ children }: { children: JSX.Element }) {
  const isAuth = authService.isAuthenticated();
  return isAuth ? children : <Navigate to="/auth" replace />;
}