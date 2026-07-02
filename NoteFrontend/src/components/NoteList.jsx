import React, { useEffect, useState } from 'react'
import { getAll } from '../service/noteService'
import NoteCard from './NoteCard';

const NoteList = ({refresh}) => {
  const [notes ,setNotes] =useState([]);
  const fetchNote=async()=>{
    try {
      const fetchs= await getAll();
      setNotes(fetchs.data);
      
    } catch (error) {
      console.error(error)
    }
  }
  useEffect(()=>{
    fetchNote();
  },[refresh])
  return (
    <div>
      {
        notes.map(note=>{
          return(<div key={note.id}>
            <NoteCard note={note} onDelete={fetchNote} />
          </div>);
        })
      }
      
    </div>
  )
}

export default NoteList
