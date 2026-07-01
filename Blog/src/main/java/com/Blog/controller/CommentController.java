package com.Blog.controller;

import com.Blog.dto.CommentDto;
import com.Blog.entity.Comment;
import com.Blog.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post/{postId}/comment")
public class CommentController {
    private final CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CommentDto>> getAll(@PathVariable long postId){
        return ResponseEntity.ok(service.getAllComment(postId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getById(@PathVariable long postId, @PathVariable long id){
        return ResponseEntity.ok(service.getCommentDtoById(postId,id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable long postId,@PathVariable long id, @RequestBody CommentDto dto){
        return ResponseEntity.ok(service.updateComment(postId,id, dto));
    }

    @PostMapping("/")
    public ResponseEntity<CommentDto> createComment(@PathVariable long postId,@RequestBody CommentDto dto){
        return ResponseEntity.ok(service.createComment(postId,dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable long postId ,@PathVariable long id){
        service.deleteComment(postId,id);
        return ResponseEntity.ok("Comment Deleted..!");
    }

}
