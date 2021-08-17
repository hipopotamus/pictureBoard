package hipo.pictureboard.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Follow {

    @Id @GeneratedValue
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loginId")
    private Member followMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member followedMember;

    @Enumerated(EnumType.STRING)
    private OneClickStatus status;

    public static Follow createFollow(Member followMember, Member followedMember) {
        Follow follow = new Follow();
        follow.setFollowMember(followMember);
        follow.setFollowedMember(followedMember);
        follow.setStatus(OneClickStatus.CLICK);
        return follow;
    }
}
