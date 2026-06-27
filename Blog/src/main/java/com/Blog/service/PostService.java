package com.Blog.service;

import com.Blog.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private final PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public void createPost(){

    }

    public void updatePost(){

    }

    public void deletePost(){

    }

    public void getByIdPost(){

    }

    public void getAllPost(){

    }

    public void getByTitlePost(){

    }

    //specifications and jpa
    public void filterPost(){

    }

}
