package hipo.pictureboard.domain;

import hipo.pictureboard.exception.NotBelowZeroException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter @Setter
public class Picture {

    @Id @GeneratedValue
    @Column(name = "picture_id")
    private Long id;

    private String title;
    private String content;

    @Enumerated(EnumType.STRING)
    private PictureType pictureType;

    private Date saveDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Embedded
    private Img picture;

    private int likeCount = 0;

    public void addLikeCount() {
        this.likeCount += 1;
    }

    public void removeLikeCount() {
        int resetLikeCount = this.likeCount - 1;
        if (resetLikeCount < 0) {
            throw new NotBelowZeroException("likeCount don't below zero");
        }
        this.likeCount = resetLikeCount;
    }
}
