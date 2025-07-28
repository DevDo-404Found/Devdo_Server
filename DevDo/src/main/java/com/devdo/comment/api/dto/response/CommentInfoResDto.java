package com.devdo.comment.api.dto.response;

import com.devdo.comment.domain.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// 대댓글 포함 댓글 상세 리스트
public record CommentInfoResDto(
        Long commentId,
        Long communityId,
        String content,
        LocalDateTime commentCreatedAt,
        String writerNickname,
        String writerPictureUrl,
        List<CommentInfoResDto> childComments
) {
    public static CommentInfoResDto from(Comment comment) {
        List<CommentInfoResDto> childComments = comment.getChildComments().stream()
                .map(CommentInfoResDto::from)
                .collect(Collectors.toList());

        return new CommentInfoResDto(
                comment.getId(),
                comment.getCommunity().getId(),
                comment.getContent(),
                comment.getCommentCreatedAt(),
                comment.getMember().getNickname(),
                comment.getMember().getPictureUrl(),
                childComments
        );
    }
}
