import React from 'react'
import { deleteNote } from '../service/noteService'

const NoteCard = ({note,onDelete}) => {
  const handleDelete=async(id)=>{
    try {
      await deleteNote(id);
      onDelete();
      alert("note deleted..!");
    } catch (error) {
      console.error(error);
    }
  }


  return (
    <div>
      <h1>{note.title}</h1>
      <p>{note.description}</p>
      <button>edit</button>
      <button onClick={()=>handleDelete(note.id)}>delete</button>
    </div>
  )
}

export default NoteCard
