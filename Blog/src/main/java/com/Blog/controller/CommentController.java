package com.Blog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment")
public class CommentController {

    @GetMapping("/all")
    public ResponseEntity<String> getAll(){
        return ResponseEntity.ok("all");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getById(){
        return ResponseEntity.ok("all");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateComment(){
        return ResponseEntity.ok("all");
    }

    @PostMapping("/")
    public ResponseEntity<String> createComment(){
        return ResponseEntity.ok("all");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteComment(){
        return ResponseEntity.ok("all");
    }

}
