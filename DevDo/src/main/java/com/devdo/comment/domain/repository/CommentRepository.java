package com.devdo.comment.domain.repository;

import com.devdo.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    int countByCommunity_Id(Long communityId);
    List<Comment> findAllByCommunity_Id(Long communityId);
}
