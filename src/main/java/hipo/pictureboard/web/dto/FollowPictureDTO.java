package hipo.pictureboard.web.dto;

import hipo.pictureboard.domain.Follow;
import hipo.pictureboard.domain.Member;
import hipo.pictureboard.domain.Picture;
import lombok.Data;

import java.util.List;

@Data
public class FollowPictureDTO {
    private Member followedMember;
    private List<Picture> followedPictures;
    boolean followCheck;
}
