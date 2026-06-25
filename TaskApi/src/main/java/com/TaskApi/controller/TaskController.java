package com.TaskApi.controller;

import com.TaskApi.Dto.Status;
import com.TaskApi.Dto.TaskDto;
import com.TaskApi.model.Task;
import com.TaskApi.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService service;

    public TaskController(TaskService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Task>> allTask(){
        return ResponseEntity.ok(service.showAllTask());
    }

    @PostMapping("/create")
    public ResponseEntity<Task> createTask(
            @RequestBody TaskDto dto
            ){
        return ResponseEntity.ok(service.addTask(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getById(
            @PathVariable long id
    ){
        return ResponseEntity.ok(service.getTaskById(id));
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<Task> getByTitle(
            @PathVariable String title
    ){
        return ResponseEntity.ok(service.getBYTitle(title));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(
            @PathVariable long id
    ){
        service.deleteTAsk(id);
        return ResponseEntity.ok("Deleted..!");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Task> update(
            @PathVariable long id,
            @RequestBody TaskDto dto
    ){
        return ResponseEntity.ok(service.updateTask(id,dto));
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<Task> updateStatus(
            @PathVariable long id,
            @RequestBody Status status
            ){
        return ResponseEntity.ok(service.updateStatus(id,status));
    }

}
