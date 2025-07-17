package com.devdo.global.nickname;

import com.devdo.member.domain.Member;
import com.devdo.member.domain.SocialType;
import com.devdo.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RandomNicknameService {

    private final MemberRepository memberRepository;

    public Member findOrCreateMember(String email, String pictureUrl, SocialType socialType) {
        return memberRepository.findByEmail(email).orElseGet(() -> {
            String nickname = generateUniqueNickname();
            Member member = Member.builder()
                    .email(email)
                    .nickname(nickname)
                    .pictureUrl(pictureUrl)
                    .socialType(socialType)
                    .build();
            return memberRepository.save(member);
        });
    }

    private String generateUniqueNickname() {
        String nickname;
        int maxTries = 10;

        do {
            nickname = NicknameGenerator.generateNickname();
            maxTries--;
        } while (memberRepository.existsByNickname(nickname) && maxTries > 0);

        return nickname;
    }
}
