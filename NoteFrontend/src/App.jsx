import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from './assets/vite.svg'
import heroImg from './assets/hero.png'
import './App.css'
import NoteForm from './components/NoteForm'
import NoteList from './components/NoteList'

function App() {
  const[refresh,setRefresh]=useState(false);
  return (
    <>
    <h1 className=' font-extrabold text-2xl text-amber-600 text-center'>Notes Manager</h1>
    <NoteForm   onCreate={() => setRefresh(!refresh)}/>
    <NoteList refresh={refresh}/>   
    </>
  )
}

export default App
