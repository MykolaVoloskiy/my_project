package com.example.my_project.service;

import com.example.my_project.dto.PostDto;
import com.example.my_project.dto.UserDto;
import com.example.my_project.entity.Post;
import com.example.my_project.entity.PostLike;
import com.example.my_project.entity.User;
import com.example.my_project.enums.Roles;
import com.example.my_project.mappers.PostsMapper;
import com.example.my_project.mappers.UserMapper;
import com.example.my_project.repository.PostLikeRepository;
import com.example.my_project.repository.PostRepository;
import com.example.my_project.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl extends AbstractService implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    public PostServiceImpl(PostRepository postRepository,
                           UserRepository userRepository,
                           PostLikeRepository postLikeRepository) {
        super(userRepository);
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postLikeRepository = postLikeRepository;
    }

    @Override
    public PostDto create(PostDto postDto) {
        Post post = PostsMapper.toEntity(postDto);
        UserDto authUser = getAuthUser();
        post.setUserId(authUser.getId());
        Post save = postRepository.save(post);
        return PostsMapper.toDto(save);
    }

    @Override
    public List<PostDto> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(post -> {
                    Long userId = post.getUserId();
                    User user = userRepository.getById(userId);
                    PostDto postDto = PostsMapper.toDto(post, UserMapper.toDto(user));
                    postDto.setLikeCount(postLikeRepository.countByPost_IdAndLikedIsTrue(post.getId()));
                    postDto.setCanRemove(canRemovePost(post));
                    return postDto;
                })
                .collect(Collectors.toList());
    }

    private boolean canRemovePost(Post post) {
        boolean canRemove;
        try {
            UserDto currentUser = getAuthUser();
            if (post.getUserId().equals(currentUser.getId()) || currentUser.getRole().contains(Roles.ROLE_ADMIN)) {
                canRemove = true;
            } else {
                canRemove = false;
            }
        } catch (Exception e) {
            canRemove = false;
        }

        return canRemove;
    }

    @Override
    public void doLikeOperation(long postId) {
        UserDto userDto = getAuthUser();

        Optional<PostLike> postLikeOptional = postLikeRepository.findByUser_IdAndPost_Id(userDto.getId(), postId);

        postLikeOptional.ifPresent(postLike -> {
            postLike.setLiked(!postLike.isLiked());
            postLikeRepository.save(postLike);
        });

        if (!postLikeOptional.isPresent()) {
            PostLike postLike = new PostLike();
            postLike.setPost(postRepository.getById(postId));
            postLike.setUser(userRepository.getById(userDto.getId()));
            postLikeRepository.save(postLike);
        }

    }

    @Override
    public void delete(long postId) throws AccessDeniedException {
        Post post = postRepository.getById(postId);
        UserDto userDto = getAuthUser();
        Set<Roles> roles = userDto.getRole();
        if (post.getUserId().equals(userDto.getId()) || roles.contains(Roles.ROLE_ADMIN)) {
            post.setRemoved(true);
            postRepository.save(post);
        } else {
            throw new AccessDeniedException("You cant delete this post");
        }

    }

    @Override
    public PostDto getById(Long postId) {
        Post post = postRepository.getById(postId);
        Long userId = post.getUserId();
        User user = userRepository.getById(userId);
        UserDto userDto = UserMapper.toDto(user);
        PostDto postDto = PostsMapper.toDto(post, userDto);
        postDto.setLikeCount(postLikeRepository.countByPost_IdAndLikedIsTrue(post.getId()));
        return postDto;
    }
}
