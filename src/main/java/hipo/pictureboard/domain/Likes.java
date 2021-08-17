package hipo.pictureboard.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter @Setter
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
    private OneClickStatus status;

    public static Likes createLikes(Member member, Picture picture) {
        Likes likes = new Likes();
        likes.setMember(member);
        likes.setPicture(picture);
        likes.setStatus(OneClickStatus.CLICK);
        return likes;
    }
}
