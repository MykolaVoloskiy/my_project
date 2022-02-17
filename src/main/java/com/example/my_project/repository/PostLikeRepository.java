package com.example.my_project.repository;

import com.example.my_project.entity.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    long countByPost_IdAndLikedIsTrue(Long postId);

    Optional<PostLike> findByUser_IdAndPost_Id(long userId, long postId);
}
