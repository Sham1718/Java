package com.Blog.service;

import com.Blog.dto.CommentDto;
import com.Blog.entity.Comment;
import com.Blog.entity.Post;
import com.Blog.exception.CommentNotFound;
import com.Blog.exception.PostNotFound;
import com.Blog.repository.CommentRepository;
import com.Blog.repository.PostRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CommentService {
    private final CommentRepository repository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public CommentService(CommentRepository repository, PostRepository postRepository, ModelMapper modelMapper) {
        this.repository = repository;
        this.postRepository = postRepository;
        this.modelMapper = modelMapper;
    }

    public CommentDto createComment(long postId,CommentDto dto){
        Post post=postRepository.findById(postId).orElseThrow(()->new PostNotFound("Post Not Found..!"));
        Comment comment = modelMapper.map(dto,Comment.class);
        comment.setPost(post);
        Comment saved=repository.save(comment);
        return modelMapper.map(saved,CommentDto.class);
    }

    public CommentDto updateComment(long postId,long id, CommentDto dto){
           Comment comment = getCommentById(postId,id);
           modelMapper.map(dto,comment);
           Comment saved= repository.save(comment);
           return modelMapper.map(saved,CommentDto.class);
    }

    public void deleteComment(long postId, long id){
        Comment comment = getCommentById(postId,id);
        repository.delete(comment);
    }

    public Comment getCommentById(long postId,long id){
        Post post=postRepository.findById(postId).orElseThrow(()->new PostNotFound("Post Not Found..!"));
        return repository.findByPostIdAndId(postId,id).orElseThrow(()->new CommentNotFound("Comment not found..!"));
    }

    public CommentDto getCommentDtoById(long postId,long id){
        Comment comment= getCommentById(postId,id);
        return modelMapper.map(comment,CommentDto.class);

    }

    public List<CommentDto> getAllComment(long postId){
        Post post=postRepository.findById(postId).orElseThrow(()->new PostNotFound("Post Not Found..!"));
        return post.getComments().stream().
                map((comment)->modelMapper.map(comment,CommentDto.class))
                .toList();
    }
}
