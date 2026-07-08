import { useState } from 'react'
import { BrowserRouter } from 'react-router-dom'
import { Route,Routes } from 'react-router-dom'
import './App.css'
import Navbar from './Components/Navbar'
import Home from './pages/Home'
import AddTask from './pages/AddTask'
import EditTask from './pages/EditTask'
import TaskDetails from './pages/TaskDetails'

function App() {
  

  return (
   <BrowserRouter>  
    <Routes>
      <Route path='/' element={<Home/>} />
      <Route path='/create' element= {<AddTask/>} />
      <Route path='/edit/:id' element={<EditTask/>} />
      <Route path='/task/:id' element={<TaskDetails/>}/>
   </Routes>
   </BrowserRouter>
  )
}

export default App
