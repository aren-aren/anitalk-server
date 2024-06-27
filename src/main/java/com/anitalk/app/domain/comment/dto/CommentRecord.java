package com.anitalk.app.domain.comment.dto;

import com.anitalk.app.domain.comment.CommentEntity;
import com.anitalk.app.utils.IpFormatter;

public record CommentRecord(
        Long id,
        Long userId,
        Long boardId,
        String content,
        String writeDate,
        String nickname,
        String password,
        String ip,
        Long refId,
        Long depth,
        Long step,
        String isDelete
) {
    public static CommentRecord of(CommentEntity entity){
        String ip = IpFormatter.format(entity.getIp());
        String content = entity.getContent();
        if(entity.getIsDeleted()){
            content = "삭제된 댓글입니다.";
        }

        return new CommentRecord(
                entity.getId(),
                entity.getUserId(),
                entity.getBoard().getId(),
                content,
                entity.getWriteDate(),
                entity.getNickname(),
                entity.getPassword(),
                ip,
                entity.getRefId(),
                entity.getDepth(),
                entity.getStep(),
                entity.getIsDeleted().toString()
        );
    }
}
