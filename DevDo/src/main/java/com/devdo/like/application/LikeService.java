package com.devdo.like.application;

import com.devdo.common.error.ErrorCode;
import com.devdo.common.exception.BusinessException;
import com.devdo.community.entity.Community;
import com.devdo.community.repository.CommunityRepository;
import com.devdo.like.domain.Like;
import com.devdo.like.domain.repository.LikeRepository;
import com.devdo.member.domain.Member;
import com.devdo.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeService {

    private final LikeRepository likeRepository;
    private final MemberRepository memberRepository;
    private final CommunityRepository communityRepository;

    // 좋아요 생성
    public void saveLike(Long communityId, Principal principal) {
        Member member = getMemberFromPrincipal(principal);
        Community community = getCommunity(communityId);

        // 좋아요 이미 존재 예외
        if (likeRepository.existsByMemberAndCommunity(member, community)) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS_LIKE,
                    ErrorCode.ALREADY_EXISTS_LIKE.getMessage());
        }

        // 좋아요 생성
        Like like = new Like(member, community);
        likeRepository.save(like);

        // 좋아요 개수 증가
        community.updateLikeCount(1);
    }

    // 좋아요 취소
    public void deleteLike(Long communityId, Principal principal) {
        Member member = getMemberFromPrincipal(principal);
        Community community = getCommunity(communityId);

        // like 찾기
        Like like = likeRepository.findByMemberAndCommunity(member, community).orElseThrow(
                () -> new BusinessException(ErrorCode.LIKE_NOT_FOUND_EXCEPTION,
                        ErrorCode.LIKE_NOT_FOUND_EXCEPTION.getMessage()));

        // 좋아요 삭제
        likeRepository.delete(like);

        // 좋아요 개수 감소
        community.updateLikeCount(-1);
    }

    // entity 찾는 공통 메소드 - member
    private Member getMemberFromPrincipal(Principal principal) {
        Long id = Long.parseLong(principal.getName());

        return memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND_EXCEPTION
                        , ErrorCode.MEMBER_NOT_FOUND_EXCEPTION.getMessage() + id));
    }

    // entity 찾는 공통 메소드 - community
    private Community getCommunity(Long id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMUNITY_NOT_FOUND_EXCEPTION,
                        ErrorCode.COMMUNITY_NOT_FOUND_EXCEPTION.getMessage() + id));
    }
}
