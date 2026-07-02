import React from 'react'
import { useState } from 'react';
import { createNote } from '../service/noteService';


const NoteForm = ({onCreate}) => {

  const [title,setTitle] = useState("");
  const [description,setDescription] = useState("");
  const handleSubmit =async()=>{
    const note={
        title,
        description
      }
    try {
      await createNote(note);
      alert("note created");
      setTitle("");
      setDescription("");
      onCreate();
    } catch (error) {
      console.error(error);
    }
  }
  return (
    <div>
      <h1>title :</h1>
      <input type="text" placeholder='enter title' value={title} onChange={(e)=>setTitle(e.target.value)} />
      <h1>description</h1>
      <input type="text" placeholder='enter description' value={description} onChange={(e)=>setDescription(e.target.value)}/>
      <input type="button" value="sumbit" onClick={handleSubmit}  />
    </div>
  )
}

export default NoteForm
