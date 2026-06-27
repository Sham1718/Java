package com.Blog.service;

import com.Blog.repository.CommentRepository;
import org.springframework.stereotype.Service;


@Service
public class CommentService {
    private final CommentRepository repository;

    public CommentService(CommentRepository repository) {
        this.repository = repository;
    }

    public void createComment(){

    }

    public void updateComment(){

    }

    public void deleteComment(){

    }

    public void getByIdComment(){

    }

    public void getAllComment(){

    }
}
