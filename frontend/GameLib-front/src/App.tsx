
import { Route, Routes } from 'react-router-dom'
import './App.css'
import { Auth } from './pages/Auth'
import { PrivateRoute } from './routes/PrivateRoute'
import { Dashboard } from './pages/Dashboard'

function App() {

  return (
    <Routes>
      <Route path="/auth"  element={<Auth/>}/>
      <Route path="/" element={<Auth/>}/>
      <Route 
        path="/dashboard" 
        element={
          <PrivateRoute>
            <Dashboard />
          </PrivateRoute>
        } 
      />
    </Routes>
  )
}

export default App
