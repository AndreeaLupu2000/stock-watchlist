import './App.css'
import LoginView from './view/LoginView'
import SearchView from './view/SearchView'
import { BrowserRouter, Routes, Route } from 'react-router-dom'

function App() {

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<LoginView />} />
        <Route path="/search" element={<SearchView />} />
      </Routes>
    </BrowserRouter>
  )
}

export default App
