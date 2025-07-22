package com.devdo.follow.domain.repository;

import com.devdo.follow.domain.Follow;
import com.devdo.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    boolean existsByFromMemberAndToMember(Member fromMember, Member toMember);
    Optional<Follow> findByFromMemberAndToMember(Member fromMember, Member toMember);
    @Query("SELECT f.toMember FROM Follow f WHERE f.fromMember = :member")
    List<Member> findFollowings(@Param("member") Member member);
    @Query("SELECT f.fromMember FROM Follow f WHERE f.toMember = :member")
    List<Member> findFollowers(@Param("member") Member member);
}
