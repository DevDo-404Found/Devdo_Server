package com.devdo.community.service;

import com.devdo.common.error.ErrorCode;
import com.devdo.common.exception.BusinessException;
import com.devdo.community.controller.dto.request.CommunityRequestDto;
import com.devdo.community.entity.Community;
import com.devdo.community.repository.CommunityRepository;
import com.devdo.member.domain.Member;
import com.devdo.member.domain.repository.MemberRepository;
import com.devdo.scrap.repository.ScrapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final MemberRepository memberRepository;
    private final ScrapRepository scrapRepository;

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
    public List<Community> getAllCommunities() {
        return communityRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Community> getMyCommunities(Principal principal) {
        Member member = getMemberFromPrincipal(principal);
        return communityRepository.findAllByMember_MemberId(member.getMemberId());
    }

    @Transactional(readOnly = true)
    public List<Community> searchCommunitiesByTitle(String keyword) {
        return communityRepository.findByTitleContainingIgnoreCase(keyword);
    }
}
