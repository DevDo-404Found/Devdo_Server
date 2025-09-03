package com.devdo.comment.api.dto.response;

import com.devdo.comment.domain.Comment;
import com.devdo.member.util.MemberInfoHelper;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// 대댓글 포함 댓글 상세 리스트
public record CommentInfoResDto(
        boolean isParent,
        Long commentId,
        Long communityId,
        String content,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime commentCreatedAt,
        String writerNickname,
        String writerPictureUrl,
        List<CommentInfoResDto> childComments
) {
    public static CommentInfoResDto from(Comment comment) {
        List<CommentInfoResDto> childComments = comment.getChildComments().stream()
                .map(CommentInfoResDto::from)
                .collect(Collectors.toList());

        boolean isParent = comment.getParentComment() == null;

        return new CommentInfoResDto(
                isParent,
                comment.getId(),
                comment.getCommunity().getId(),
                comment.getContent(),
                comment.getCommentCreatedAt(),
                MemberInfoHelper.getMemberNickname(comment.getMember()),
                MemberInfoHelper.getMemberPictureUrl(comment.getMember()),
                childComments
        );
    }
}
