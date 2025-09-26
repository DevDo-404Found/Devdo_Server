package com.devdo.member.util;

import com.devdo.member.domain.Member;

public class MemberInfoHelper {
    public static String getMemberNickname(Member member) {
        if (member == null || member.isDeleted()) return "탈퇴한 회원";
        return member.getNickname();
    }

    public static String getMemberPictureUrl(Member member) {
        if (member == null || member.isDeleted()) return null;
        return member.getPictureUrl();
    }
}
