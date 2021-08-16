package hipo.pictureboard.domain;

import hipo.pictureboard.exception.NotBelowZeroException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String loginId;
    private String password;
    private String nickName;
    private int following = 0;
    private int follower = 0;

    @Embedded
    private Img profilePicture;

    public void addFollowing() {
        this.following += 1;
    }

    public void removeFollowing() {
        int resetFollowing = this.following - 1;
        if (resetFollowing < 0) {
            throw new NotBelowZeroException("following don't below zero");
        }
        this.following = resetFollowing;
    }

    public void addFollower() {
        this.follower += 1;
    }

    public void removeFollower() {
        int resetFollower = this.follower - 1;
        if (resetFollower < 0) {
            throw new NotBelowZeroException("following don't below zero");
        }
        this.following = resetFollower;
    }

}
