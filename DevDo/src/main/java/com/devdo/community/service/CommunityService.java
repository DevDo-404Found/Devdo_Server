package com.devdo.community.service;

import com.devdo.comment.domain.repository.CommentRepository;
import com.devdo.common.error.ErrorCode;
import com.devdo.common.exception.BusinessException;
import com.devdo.community.controller.dto.request.CommunityRequestDto;
import com.devdo.community.controller.dto.response.CommunityAllResponseDto;
import com.devdo.community.entity.Community;
import com.devdo.community.repository.CommunityRepository;
import com.devdo.member.domain.Member;
import com.devdo.member.domain.repository.MemberRepository;
import com.devdo.scrap.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;
    private final ScrapRepository scrapRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final CommentRepository commentRepository;

    // 공통 메서드
    @Transactional
    public Community findCommunityById(Long id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMUNITY_NOT_FOUND_EXCEPTION
                        , ErrorCode.COMMUNITY_NOT_FOUND_EXCEPTION.getMessage() + id));
    }

    @Transactional(readOnly = true)
    public Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND_EXCEPTION
                        , ErrorCode.MEMBER_NOT_FOUND_EXCEPTION.getMessage() + id));
    }

    @Transactional(readOnly = true)
    public Member getMemberFromPrincipal(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new BusinessException(ErrorCode.FORBIDDEN_EXCEPTION
                    , ErrorCode.FORBIDDEN_EXCEPTION.getMessage());
        }
        try {
            Long memberId = Long.parseLong(principal.getName());
            return findMemberById(memberId);
        } catch (NumberFormatException e) {
            throw new BusinessException(ErrorCode.FORBIDDEN_EXCEPTION
                    , ErrorCode.FORBIDDEN_EXCEPTION.getMessage());
        }
    }

    // 주요 서비스 메서드
    @Transactional
    public Long createCommunity(CommunityRequestDto commnuityRequestDto, Principal principal) {
        Member member = getMemberFromPrincipal(principal);

        Community community = Community.builder()
                .title(commnuityRequestDto.title())
                .content(commnuityRequestDto.content())
                .member(member)
                .build();

        return communityRepository.save(community).getId();
    }

    @Transactional
    public Community updateCommunity(Long communityId, CommunityRequestDto commnuityRequestDto, Principal principal) {
        Community community = findCommunityById(communityId);
        Member member = getMemberFromPrincipal(principal);

        if (!community.getMember().getMemberId().equals(member.getMemberId())) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, ErrorCode.NO_AUTHORIZATION_EXCEPTION.getMessage());
        }

        community.update(commnuityRequestDto);
        return community;
    }

    @Transactional
    public void deleteCommunity(Long communityId, Principal principal) {
        Community community = findCommunityById(communityId);
        Member member = getMemberFromPrincipal(principal);

        if (!community.getMember().getMemberId().equals(member.getMemberId())) {
            throw new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, ErrorCode.NO_AUTHORIZATION_EXCEPTION.getMessage());
        }

        // Scrap 먼저 삭제
        scrapRepository.deleteAllByCommunity(community);

        communityRepository.delete(community);
    }

    @Transactional(readOnly = true)
    public Community getCommunity(Long id) {
        return communityRepository.findWithMemberById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMUNITY_NOT_FOUND_EXCEPTION, ErrorCode.COMMUNITY_NOT_FOUND_EXCEPTION.getMessage() + id));
    }

    @Transactional(readOnly = true)
    public List<CommunityAllResponseDto> getAllCommunities() {
        List<Community> communities = communityRepository.findAll();

        return communities.stream()
                .map(community -> {
                    int commentCount = commentRepository.countByCommunity_Id(community.getId());
                    return CommunityAllResponseDto.from(community, commentCount);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CommunityAllResponseDto> getMyCommunities(Principal principal) {
        Member member = getMemberFromPrincipal(principal);
        List<Community> communities = communityRepository.findAllByMember_MemberId(member.getMemberId());

        return communities.stream()
                .map(community -> {
                    int commentCount = commentRepository.countByCommunity_Id(community.getId());
                    return CommunityAllResponseDto.from(community, commentCount);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CommunityAllResponseDto> searchCommunitiesByTitle(String keyword) {
        List<Community> communities = communityRepository.findByTitleContainingIgnoreCase(keyword);

        return communities.stream()
                .map(community -> {
                    int commentCount = commentRepository.countByCommunity_Id(community.getId());
                    return CommunityAllResponseDto.from(community, commentCount);
                })
                .toList();
    }

    // 댓글 개수
    @Transactional(readOnly = true)
    public int getCommentCountByCommunityId(Long communityId) {
        return commentRepository.countByCommunity_Id(communityId);
    }

    // Redis 조회수
    @Transactional
    public Community getCommunityWithRedisViewCount(Long communityId, Principal principal) {
        Community community = getCommunity(communityId);
        String memberId = principal.getName();

        // redisKey 생성
        String redisKey = "community:view:" + memberId + ":" + communityId;

        Boolean hasViewed = stringRedisTemplate.hasKey(redisKey);
        if (hasViewed == null || !hasViewed) {
            community.increaseViewCount();

            // 24시간 후 만료
            stringRedisTemplate.opsForValue().set(redisKey, "1", Duration.ofHours(24));
        }
        return community;
    }
}
