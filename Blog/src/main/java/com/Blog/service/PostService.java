package com.Blog.service;

import com.Blog.dto.PostDto;
import com.Blog.entity.Post;
import com.Blog.exception.PostNotFound;
import com.Blog.repository.PostRepository;
import com.Blog.specification.PostSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository repository;
    private final ModelMapper modelMapper;

    public PostService(PostRepository repository,
                       ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public Post createPost(PostDto dto){
        Post post=modelMapper.map(dto,Post.class);
        return repository.save(post);
    }

    public Post updatePost(long id,PostDto dto){
        Post post=getByIdPost(id);
        modelMapper.map(dto,post);
        return repository.save(post);
    }

    public void deletePost(long id){
        Post post=repository.findById(id).orElseThrow(()->new PostNotFound("Post Not Found..!"));
        repository.delete(post);
    }

    public Post getByIdPost(long id){
        return repository.findById(id).orElseThrow(()->new PostNotFound("Post Not found..!"));

    }

    public Page<PostDto> getAllPost(int page, int size){
        Pageable pageable= PageRequest.of(page,size);
        Page<Post> posts=repository.findAll(pageable);
        return posts.map(post->modelMapper.map(post,PostDto.class));
    }

    public Post getByTitlePost(String title){
        return repository.findByTitle(title).orElseThrow(()->new PostNotFound("Post Not Found...!"));
    }

    public List<Post> filterPost(
            String title,String author
    ){
        Specification<Post> spec=((root,
                                   query, criteriaBuilder) -> criteriaBuilder.conjunction());

        if (title!=null){
            spec=spec.and(PostSpecification.hasTitle(title));
        }
        if (author!=null){
            spec=spec.and(PostSpecification.hasAuthor(author));
        }
        return repository.findAll(spec);
    }

}
