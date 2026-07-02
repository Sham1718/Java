import axios from "axios";

const api=axios.create({baseURL:"http://localhost:8082/notes"})

export const getAll =()=>{
    return api.get("/");
}

export const createNote =(note)=>{ 
    return  api.post("/",note)
}

export const updateNote =(id , note)=> {
     return api.put(`/${id}`,note);
}

export const deleteNote =(id)=> {
    return api.delete(`/${id}`);
}

export const getByTitle =(title)=>{
    return  api.get(`title?title=${title}`);
}
