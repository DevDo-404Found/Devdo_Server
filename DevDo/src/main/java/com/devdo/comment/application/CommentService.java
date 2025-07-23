package com.devdo.comment.application;

import com.devdo.comment.api.dto.request.CommentSaveReqDto;
import com.devdo.comment.api.dto.request.CommentUpdateReqDto;
import com.devdo.comment.api.dto.response.CommentInfoResDto;
import com.devdo.comment.api.dto.response.CommentListResDto;
import com.devdo.comment.domain.Comment;
import com.devdo.comment.domain.repository.CommentRepository;
import com.devdo.common.error.ErrorCode;
import com.devdo.common.exception.BusinessException;
import com.devdo.community.entity.Community;
import com.devdo.community.repository.CommunityRepository;
import com.devdo.member.domain.Member;
import com.devdo.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final CommunityRepository communityRepository;

    // 댓글 전체 조회
    public CommentListResDto getCommentList(Long communityId, Principal principal) {
        Member member = getMemberFromPrincipal(principal);
        List<Comment> comments = commentRepository.findAllByCommunity_Id(communityId);

        // 댓글 개수 조회
        int totalCount = commentRepository.countByCommunity_Id(communityId);
        return CommentListResDto.from(totalCount, comments , member);
    }

    // 댓글 생성
    @Transactional
    public CommentInfoResDto saveComment(Long communityId, CommentSaveReqDto commentSaveReqDto, Principal principal) {
        Member member = getMemberFromPrincipal(principal);
        Community community = getCommunity(communityId);

        // 댓글 생성
        Comment comment = Comment.builder()
                .content(commentSaveReqDto.content())
                .community(community)
                .member(member)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return CommentInfoResDto.from(savedComment);
    }

    // 댓글 수정
    @Transactional
    public CommentInfoResDto updateComment(Long commentId, CommentUpdateReqDto commentUpdateReqDto, Principal principal) {
        Member member = getMemberFromPrincipal(principal);
        Comment comment = getComment(commentId);

        // 권한 확인
        if (!comment.getMember().getMemberId().equals(member.getMemberId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN_EXCEPTION
                    , ErrorCode.FORBIDDEN_EXCEPTION.getMessage());
        }

        comment.updateComment(commentUpdateReqDto.content());
        return CommentInfoResDto.from(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId, Principal principal) {
        Member member = getMemberFromPrincipal(principal);
        Comment comment = getComment(commentId);

        // 권한 확인
        if (!comment.getMember().getMemberId().equals(member.getMemberId())) {
            throw new BusinessException(ErrorCode.FORBIDDEN_EXCEPTION
                    , ErrorCode.FORBIDDEN_EXCEPTION.getMessage());
        }

        commentRepository.delete(comment);
    }

    // entity 찾는 공통 메소드 - 로그인한 사용자 찾기
    private Member getMemberFromPrincipal(Principal principal) {
        Long id = Long.parseLong(principal.getName());

        return memberRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND_EXCEPTION
                        , ErrorCode.MEMBER_NOT_FOUND_EXCEPTION.getMessage() + id));
    }

    // entity 찾는 공통 메소드 - community 찾기
    private Community getCommunity(Long id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMUNITY_NOT_FOUND_EXCEPTION,
                        ErrorCode.COMMUNITY_NOT_FOUND_EXCEPTION.getMessage() + id));
    }

    // entity 찾는 공통 메소드 - comment 찾기
    private Comment getComment(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND_EXCEPTION,
                    ErrorCode.COMMENT_NOT_FOUND_EXCEPTION.getMessage() + id));
    }
}
