package hipo.pictureboard.domain;

import hipo.pictureboard.exception.NotBelowZeroException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    private LocalDateTime saveDate;

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

    public static Picture createPicture(String title, String content, PictureType pictureType, Member member,
                                        Img pictureImg) {
        Picture picture = new Picture();
        picture.setTitle(title);
        picture.setContent(content);
        picture.setPictureType(pictureType);
        picture.setMember(member);
        picture.setPicture(pictureImg);
        picture.setSaveDate(LocalDateTime.now());
        return picture;
    }
}
