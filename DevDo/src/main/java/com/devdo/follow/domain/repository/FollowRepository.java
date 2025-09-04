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

    // 팔로우, 팔로잉 리스트 (탈퇴 회원 보이지 않게 필터링)
    @Query("SELECT f.toMember FROM Follow f WHERE f.fromMember = :member AND f.toMember.isDeleted = false")
    List<Member> findFollowings(@Param("member") Member member);
    @Query("SELECT f.fromMember FROM Follow f WHERE f.toMember = :member AND f.fromMember.isDeleted = false")
    List<Member> findFollowers(@Param("member") Member member);

    // 팔로우 수, 팔로잉 수 카운트 (탈퇴 회원 수 포함하지 않게 필터링)
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.fromMember = :member AND f.toMember.isDeleted = false")
    int countFollowings(@Param("member") Member member);
    @Query("SELECT COUNT(f) FROM Follow f WHERE f.toMember = :member AND f.fromMember.isDeleted = false")
    int countFollowers(@Param("member") Member member);
}
