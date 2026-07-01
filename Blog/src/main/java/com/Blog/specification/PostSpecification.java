package com.Blog.specification;

import com.Blog.entity.Post;
import org.springframework.data.jpa.domain.Specification;

public class PostSpecification {
    public static Specification<Post> hasTitle(String title){

        return ((root, query, cb) ->
                cb.like(cb.lower(root.get("title")),"%"+title.toLowerCase()+"%") );
    }

    public static Specification<Post> hasAuthor(String author){
        return ((root, query, cb) ->
                cb.like(cb.lower(root.get("author")),"%"+author.toLowerCase()+"%") );
    }

}

