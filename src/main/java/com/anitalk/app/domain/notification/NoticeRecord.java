package com.anitalk.app.domain.notification;

import com.anitalk.app.domain.user.dto.UsernameRecord;

public record NoticeRecord(
        UsernameRecord from,
        NoticeContent target,
        NoticeContent content,
        NoticeType type,
        String date
) {
}
