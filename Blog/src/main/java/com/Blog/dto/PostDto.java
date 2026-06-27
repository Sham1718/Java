package com.Blog.dto;

import com.Blog.entity.Comment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class PostDto {
    private String title;
    private String content;
    private String author;
    List<Comment> comments;
}
