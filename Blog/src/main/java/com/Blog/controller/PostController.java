package com.Blog.controller;

import com.Blog.dto.PostDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
public class PostController {

    @GetMapping("/all")
    public ResponseEntity<String> getAll(){
        return ResponseEntity.ok("all");
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getByID(){
        return ResponseEntity.ok("all");
    }

    @PostMapping("/")
    public ResponseEntity<String> createPOst(){
        return ResponseEntity.ok("all");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updatePost(){
        return ResponseEntity.ok("all");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> DeleteById(){
        return ResponseEntity.ok("all");
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<String> getByTitle(){
        return ResponseEntity.ok("all");
    }
    @GetMapping("/filter")
    public ResponseEntity<String> filterPost(){
        return ResponseEntity.ok("all");
    }

}
