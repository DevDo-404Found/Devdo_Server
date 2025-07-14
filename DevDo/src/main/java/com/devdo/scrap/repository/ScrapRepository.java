package com.devdo.scrap.repository;

import com.devdo.community.entity.Community;
import com.devdo.member.domain.Member;
import com.devdo.scrap.entity.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    boolean existsByMemberAndCommunity(Member member, Community community);
    Optional<Scrap> findByMemberAndCommunity(Member member, Community community);
    List<Scrap> findAllByMember(Member member);
    void deleteAllByCommunity(Community community);
}
