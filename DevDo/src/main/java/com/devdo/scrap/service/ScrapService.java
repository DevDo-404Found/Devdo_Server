package com.devdo.scrap.service;

import com.devdo.common.error.ErrorCode;
import com.devdo.common.exception.BusinessException;
import com.devdo.community.entity.Community;
import com.devdo.community.repository.CommunityRepository;
import com.devdo.member.domain.Member;
import com.devdo.member.domain.repository.MemberRepository;
import com.devdo.scrap.controller.dto.ScrapResponseDto;
import com.devdo.scrap.entity.Scrap;
import com.devdo.scrap.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;
    private final ScrapRepository scrapRepository;

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

    @Transactional
    public Long saveScrap(Long id, Principal principal) {
        Member member = getMemberFromPrincipal(principal);
        Community community = findCommunityById(id);

        if (scrapRepository.existsByMemberAndCommunity(member, community)) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS_EMAIL, "이미 스크랩한 게시글입니다.");
        }

        Scrap scrap = Scrap.builder()
                .member(member)
                .community(community)
                .build();

        community.increaseScrapCount();

        return scrapRepository.save(scrap).getId();
    }

    @Transactional
    public void deletesScrap(Long communityId, Principal principal) {
        Member member = getMemberFromPrincipal(principal);
        Community community = findCommunityById(communityId);

        Scrap scrap = scrapRepository.findByMemberAndCommunity(member, community)
                .orElseThrow(() -> new BusinessException(ErrorCode.NO_AUTHORIZATION_EXCEPTION, ErrorCode.NO_AUTHORIZATION_EXCEPTION.getMessage()));

        community.decreaseScrapCount();

        scrapRepository.delete(scrap);
    }

    @Transactional(readOnly = true)
    public Long getScrapCount(Long communityId) {
        Community community = findCommunityById(communityId);
        return community.getScrapCount();
    }

    @Transactional(readOnly = true)
    public List<ScrapResponseDto> getMyScraps(Principal principal) {
        Member member = getMemberFromPrincipal(principal);
        return scrapRepository.findAllByMember(member)
                .stream()
                .map(ScrapResponseDto::from)
                .collect(Collectors.toList());
    }
}
