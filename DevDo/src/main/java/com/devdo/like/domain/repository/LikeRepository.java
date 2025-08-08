package com.devdo.like.domain.repository;

import com.devdo.community.entity.Community;
import com.devdo.like.domain.Like;
import com.devdo.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {

    boolean existsByMemberAndCommunity(Member member, Community community);
    Optional<Like> findByMemberAndCommunity(Member member, Community community);
}
