package com.devdo.follow.domain.repository;

import com.devdo.follow.domain.Follow;
import com.devdo.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFromMemberAndToMember(Member fromMember, Member toMember);

    Optional<Follow> findByFromMemberAndToMember(Member fromMember, Member toMember);
}
