package hipo.pictureboard.domain;

import hipo.pictureboard.exception.NotBelowZeroException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
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
        this.follower = resetFollower;
    }

    public Member(String loginId, String password, String nickName, Img profilePicture) {
        this.loginId = loginId;
        this.password = password;
        this.nickName = nickName;
        this.profilePicture = profilePicture;
    }

    protected Member() {
    }
}
