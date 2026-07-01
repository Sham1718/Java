package com.Blog.controller;

import com.Blog.dto.PostDto;
import com.Blog.entity.Post;
import com.Blog.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Post>> getAll(@RequestParam(defaultValue = "0") int page ,
                                             @RequestParam(defaultValue = "10") int size){
        return ResponseEntity.ok(service.getAllPost(page,size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> getByID(
            @PathVariable long id
    ){
        return ResponseEntity.ok(service.getByIdPost(id));
    }

    @PostMapping("/")
    public ResponseEntity<Post> createPOst(
            @RequestBody PostDto dto
    ){
        return ResponseEntity.ok(service.createPost(dto));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable long id,@RequestBody PostDto dto){
        return ResponseEntity.ok(service.updatePost(id,dto));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> DeleteById(@PathVariable long id){
        service.deletePost(id);
        return ResponseEntity.ok("Deleted Post");
    }

    @GetMapping("/title")
    public ResponseEntity<Post> getByTitle(
            @RequestParam String title
    ){
        return ResponseEntity.ok(service.getByTitlePost(title));
    }
    @GetMapping("/filter")
    public ResponseEntity<List<Post>> filterPost(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author

    ){
        return ResponseEntity.ok(service.filterPost(title,author));
    }

}
