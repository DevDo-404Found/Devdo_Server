package com.devdo.follow.domain;

import com.devdo.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long followId;

    /*
    한 명의 멤버는 여러 명의 팔로잉을 가질 수 있다.
    팔로우를 요청 하는 사용자
    */
    @ManyToOne
    @JoinColumn(name = "from_member_id")
    private Member fromMember;

    /*
    한 명의 멤버는 여러 명의 팔로워를 가질 수 있다.
    팔로우를 요청 받은 사용자
    */
    @ManyToOne
    @JoinColumn(name = "to_member_id")
    private Member toMember;

    @Column(name = "follow_created_at")
    private LocalDateTime followCreatedAt;

    @Builder
    public Follow(Member fromMember, Member toMember) {
        this.fromMember = fromMember;
        this.toMember = toMember;
        this.followCreatedAt = LocalDateTime.now();
    }
}
