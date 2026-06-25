package com.TaskApi.service;

import com.TaskApi.Dto.Status;
import com.TaskApi.Dto.TaskDto;
import com.TaskApi.exception.TaskNotFound;
import com.TaskApi.model.Task;
import com.TaskApi.repository.TaskRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepo repo;

    public TaskService(TaskRepo repo) {
        this.repo = repo;
    }

    public Task addTask(TaskDto dto){
        Task task=new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setComplete(dto.isComplete());

        return repo.save(task);
    }

    public Task updateTask(long id, TaskDto dto){
        Task task =getTaskById(id);
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setComplete(dto.isComplete());

        return repo.save(task);
    }

    public List<Task> showAllTask(){
        return  repo.findAll();
    }

    public void deleteTAsk(long id){
        Task task=getTaskById(id);
        repo.delete(task);
    }

    public Task updateStatus(long id, Status status){
        Task task =getTaskById(id);
        task.setComplete(status.isComplete());
        return repo.save(task);

    }

    public Task getBYTitle(String title){
        return repo.findByTitleIgnoreCase(title).orElseThrow(()
                ->new TaskNotFound("Task Not Found..!"));
    }
    public Task getTaskById(long id){
        return  repo.findById(id).orElseThrow(()
                ->new TaskNotFound("Task not Found..!"));
    }


}
