package com.devdo.follow.application;

import com.devdo.common.error.ErrorCode;
import com.devdo.common.exception.BusinessException;
import com.devdo.follow.domain.Follow;
import com.devdo.follow.domain.repository.FollowRepository;
import com.devdo.member.domain.Member;
import com.devdo.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FollowService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    // 팔로우 요청
    @Transactional
    public void follow(Long toMemberId, Principal principal) {
        Member fromMember = getMemberFromPrincipal(principal);
        Member toMember = getMemberById(toMemberId);

        // 자기 자신 팔로우 불가 예외
        if (fromMember.getMemberId().equals(toMember.getMemberId())) {
            throw new BusinessException(ErrorCode.CANNOT_FOLLOW_SELF,
                    ErrorCode.CANNOT_FOLLOW_SELF.getMessage());
        }

        // 이미 팔로우 예외
        boolean alreadyFollowing = followRepository.existsByFromMemberAndToMember(fromMember, toMember);
        if (alreadyFollowing) {
            throw new BusinessException(ErrorCode.ALREADY_FOLLOW_STATE,
                    ErrorCode.ALREADY_FOLLOW_STATE.getMessage());
        }

        // follow 정보 저장
        Follow follow = Follow.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .build();
        followRepository.save(follow);

        // follow count update
        fromMember.updateFollowingCount(+1);
        toMember.updateFollowerCount(+1);
    }

    // 언팔로우 요청
    @Transactional
    public void unfollow(Long toMemberId, Principal principal) {
        Member fromMember = getMemberFromPrincipal(principal);
        Member toMember = getMemberById(toMemberId);

        Follow follow = getFollowByMember(fromMember, toMember);

        followRepository.delete(follow);

        // follow count update
        fromMember.updateFollowingCount(-1);
        toMember.updateFollowerCount(-1);
    }

    // TODO: follower, following 조회

    // entity 찾는 공통 메소드 - 로그인한 사용자 찾기
    private Member getMemberFromPrincipal(Principal principal) {
        Long id = Long.parseLong(principal.getName());

        return memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND_EXCEPTION
                        , ErrorCode.MEMBER_NOT_FOUND_EXCEPTION.getMessage()));
    }

    // entity 찾는 공통 메소드 - memberId 찾기
    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND_EXCEPTION
                , ErrorCode.MEMBER_NOT_FOUND_EXCEPTION.getMessage() + memberId));
    }

    // entity 찾는 공통 메소드 - follow 찾기
    private Follow getFollowByMember(Member fromMember, Member toMember) {
        return followRepository.findByFromMemberAndToMember(fromMember, toMember)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOLLOW_STATE,
                        ErrorCode.NOT_FOLLOW_STATE.getMessage()));

    }
}
