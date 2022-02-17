package com.example.my_project.mappers;

import com.example.my_project.dto.PostDto;
import com.example.my_project.dto.UserDto;
import com.example.my_project.entity.Post;

public class PostsMapper {

    public static PostDto toDto(Post post) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setText(post.getText());
        postDto.setTitle(post.getTitle());
        postDto.setUserId(post.getUserId());
        return postDto;
    }

    public static PostDto toDto(Post post, UserDto userDto) {
        PostDto postDto = new PostDto();
        postDto.setId(post.getId());
        postDto.setText(post.getText());
        postDto.setTitle(post.getTitle());
        postDto.setUserId(post.getUserId());
        postDto.setUserDto(userDto);
        return postDto;
    }

    public static Post toEntity(PostDto postDto) {
        Post post = new Post();
        post.setId(postDto.getId());
        post.setText(postDto.getText());
        post.setTitle(postDto.getTitle());
        post.setUserId(postDto.getUserId());
        return post;
    }

}

