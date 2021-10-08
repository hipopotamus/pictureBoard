package hipo.pictureboard.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
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
    private OnClickStatus status;

    public Follow(Member followMember, Member followedMember) {
        this.followMember = followMember;
        this.followedMember = followedMember;
        this.status = OnClickStatus.ON;
    }

    protected Follow() {
    }

    public void switchStatus() {
        if (this.status == OnClickStatus.ON) {
            this.status = OnClickStatus.OFF;
            followMember.removeFollowing();
            followedMember.removeFollower();
        } else {
            this.status = OnClickStatus.ON;
            followMember.addFollowing();
            followedMember.addFollower();
        }

    }
}
