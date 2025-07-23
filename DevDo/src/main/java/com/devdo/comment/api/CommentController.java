package com.devdo.comment.api;

import com.devdo.comment.api.dto.request.CommentSaveReqDto;
import com.devdo.comment.api.dto.request.CommentUpdateReqDto;
import com.devdo.comment.api.dto.response.CommentInfoResDto;
import com.devdo.comment.api.dto.response.CommentListResDto;
import com.devdo.comment.application.CommentService;
import com.devdo.common.error.SuccessCode;
import com.devdo.common.template.ApiResTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 전체 조회", description = "로그인한 사용자가 커뮤니티 게시글 한 개에 달린 전체 댓글 리스트를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @GetMapping("/all")
    public ApiResTemplate<CommentListResDto> getCommentList(@RequestParam Long communityId, Principal principal) {
        CommentListResDto commentListResDto = commentService.getCommentList(communityId, principal);
        return ApiResTemplate.successResponse(SuccessCode.GET_SUCCESS, commentListResDto);
    }

    @Operation(summary = "댓글 생성", description = "로그인한 사용자가 댓글을 생성합니다.\n" + "부모 댓글로 요청 보낼시에는 parentCommentId를 null로 보내면 됩니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @PostMapping
    public ApiResTemplate<CommentInfoResDto> saveComment(@RequestParam Long communityId,
                                              @RequestBody CommentSaveReqDto commentSaveReqDto,
                                              Principal principal) {
        CommentInfoResDto commentInfoResDto = commentService.saveComment(communityId, commentSaveReqDto, principal);
        return ApiResTemplate.successResponse(SuccessCode.COMMENT_SAVE_SUCCESS, commentInfoResDto);
    }

    @Operation(summary = "댓글 수정", description = "로그인한 사용자가 댓글을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @PutMapping
    public ApiResTemplate<CommentInfoResDto> updateComment(@RequestParam Long commentId,
                                                           @RequestBody CommentUpdateReqDto commentUpdateReqDto,
                                                           Principal principal) {
        CommentInfoResDto commentInfoResDto = commentService.updateComment(commentId, commentUpdateReqDto, principal);
        return ApiResTemplate.successResponse(SuccessCode.COMMENT_UPDATE_SUCCESS, commentInfoResDto);
    }

    @Operation(summary = "댓글 삭제", description = "로그인한 사용자가 댓글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "응답 생성에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다.")
    })
    @DeleteMapping
    public ApiResTemplate<Long> deleteComment(@RequestParam Long commentId, Principal principal) {
        commentService.deleteComment(commentId, principal);
        return ApiResTemplate.successResponse(SuccessCode.COMMENT_DELETE_SUCCESS, commentId);
    }
}
