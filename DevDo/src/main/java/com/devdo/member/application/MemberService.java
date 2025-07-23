package com.devdo.member.application;

import com.devdo.common.error.ErrorCode;
import com.devdo.common.exception.BusinessException;
import com.devdo.member.api.dto.request.MemberInfoUpdateReqDto;
import com.devdo.member.api.dto.response.MemberInfoResDto;
import com.devdo.member.domain.Member;
import com.devdo.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    // 마이페이지 - 내 프로필 조회
    public MemberInfoResDto getMemberInfo(Principal principal) {
        Long id = Long.parseLong(principal.getName());
        Member member = getMemberById(id);

        return MemberInfoResDto.from(member);
    }

    // 마이페이지 - 내 프로필 수정
    @Transactional
    public MemberInfoResDto updateMemberInfo(MemberInfoUpdateReqDto memberInfoUpdateReqDto, Principal principal) {
        Long id = Long.parseLong(principal.getName());
        Member member = getMemberById(id);

        String newNickname = memberInfoUpdateReqDto.nickname();

        // nickname 중복 예외
        if (!member.getNickname().equals(newNickname)) {
            if (memberRepository.existsByNickname(newNickname)) {
                throw new BusinessException(ErrorCode.ALREADY_EXISTS_NICKNAME,
                        ErrorCode.ALREADY_EXISTS_NICKNAME.getMessage());
            }
            member.updateNickname(newNickname);
        }

        // nickname update
        member.updateNickname(memberInfoUpdateReqDto.nickname());

        // pictureUrl 삭제
        String newPictureUrl = memberInfoUpdateReqDto.pictureUrl();
        if (newPictureUrl == null || newPictureUrl.isBlank()) {
            member.updatePictureUrl(null);
        }

        return MemberInfoResDto.from(member);
    }

    // entity 찾는 공통 메소드
    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND_EXCEPTION
                        , ErrorCode.MEMBER_NOT_FOUND_EXCEPTION.getMessage() + memberId));
    }
}
