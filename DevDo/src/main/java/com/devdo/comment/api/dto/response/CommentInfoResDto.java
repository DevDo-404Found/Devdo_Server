package com.devdo.comment.api.dto.response;

import com.devdo.comment.domain.Comment;

import java.time.LocalDateTime;

public record CommentInfoResDto(
        Long commentId,
        Long communityId,
        String content,
        LocalDateTime commentCreatedAt,
        String writerNickname,
        String writerPictureUrl
) {
    public static CommentInfoResDto from(Comment comment) {
        return new CommentInfoResDto(
                comment.getId(),
                comment.getCommunity().getId(),
                comment.getContent(),
                comment.getCommentCreatedAt(),
                comment.getMember().getNickname(),
                comment.getMember().getPictureUrl()
        );
    }
}
