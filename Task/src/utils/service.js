import axios from "axios";

const api=axios.create({baseURL:"http://localhost:8080/task"})

export const createTask=(task)=>api.post("/",task);

export const getAllTask=()=>api.get("/");

export const getTaskById=(id)=>api.get(`/${id}`);

export const getTaskByTitle=(title)=>api.get(`/title/${title}`);

export const updateTask=(id,task)=>api.put(`/${id}`,task);

export const updateTaskStatus=(id,status)=>api.put(`/status/${id}`,status);

export const deleteTask =(id)=>api.delete(`/${id}`);
