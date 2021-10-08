package hipo.pictureboard.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Likes {

    @Id @GeneratedValue
    @Column(name = "likes_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "picture_id")
    private Picture picture;

    @Enumerated(EnumType.STRING)
    private OnClickStatus status;

    public Likes(Member member, Picture picture) {
        this.member = member;
        this.picture = picture;
        this.status = OnClickStatus.ON;
    }

    protected Likes() {
    }

    public void switchStatus() {
        if (this.status == OnClickStatus.ON) {
            this.status = OnClickStatus.OFF;
            picture.removeLikeCount();
        } else {
            this.status = OnClickStatus.ON;
            picture.addLikeCount();
        }

    }
}
