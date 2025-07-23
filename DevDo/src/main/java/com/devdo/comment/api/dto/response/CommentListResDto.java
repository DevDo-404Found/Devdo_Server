package com.devdo.comment.api.dto.response;

import com.devdo.comment.domain.Comment;
import com.devdo.member.domain.Member;

import java.util.List;
import java.util.stream.Collectors;

// 부모 댓글 리스트 + 자식 댓글 리스트
public record CommentListResDto(
        int totalCommentCount,
        List<CommentInfoResDto> commentInfoResDtos
) {
    public static CommentListResDto from(int totalCommentCount, List<Comment> comments, Member member) {
        List<CommentInfoResDto> commentInfoResDtoList = comments.stream()
                .map(CommentInfoResDto::from)
                .collect(Collectors.toList());

        return new CommentListResDto(totalCommentCount, commentInfoResDtoList);
    }
}
