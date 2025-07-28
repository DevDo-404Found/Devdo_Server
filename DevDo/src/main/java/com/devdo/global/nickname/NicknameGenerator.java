package com.devdo.global.nickname;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NicknameGenerator {

    // 닉네임 앞 형용사
    private static final String[] adjectives = {
            "귀여운", "멋쟁이", "용감한", "행복한", "배부른", "똑똑한", "즐거운", "아름다운"
            , "놀라운", "매력적인", "훌륭한", "환상적인", "사랑스러운", "신나는"
    };

    // 형용사 뒤 명사
    private static final String[] nouns = {
            "사자", "냥이", "강아지", "토끼", "판다", "기린", "호랑이", "치타", "하마"
            ,"펭귄", "물고기", "햄스터", "거북이", "고슴도치", "곰", "상어"
    };

    // 랜덤 닉네임 생성 메소드
    public static String generateNickname() {
        String adjective = adjectives[(int) (Math.random() * adjectives.length)];
        String noun = nouns[(int) (Math.random() * nouns.length)];
        int num = (int) (Math.random() * 100);

        return adjective + noun + num;
    }
}
