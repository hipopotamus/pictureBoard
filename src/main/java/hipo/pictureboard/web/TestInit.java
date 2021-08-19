package hipo.pictureboard.web;

import hipo.pictureboard.domain.*;
import hipo.pictureboard.service.FollowService;
import hipo.pictureboard.service.LikesService;
import hipo.pictureboard.service.MemberService;
import hipo.pictureboard.service.PictureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.text.html.parser.Parser;

@Component
@RequiredArgsConstructor
public class TestInit {

    private final MemberService memberService;
    private final PictureService pictureService;
    private final LikesService likesService;
    private final FollowService followService;

    @PostConstruct
    public void init() {

        //멤버 생성
        for (int i = 1; i < 31; i++) {
            memberService.create(String.valueOf(i), String.valueOf(i), "hipo" + i, new Img("sample1", "sampleProfile1.jpeg"));
        }

        Member pictureMember = memberService.findByNickName("hipo1").get(0);
        Member likeFollowMembr1 = memberService.findByNickName("hipo2").get(0);
        Member likeFollowMembr2 = memberService.findByNickName("hipo3").get(0);

        //그림 생성
        for (int i = 1; i < 5; i++) {
            pictureService.create("title" + i, "content" + i, PictureType.SCENERY,
                    pictureMember.getId(), new Img("samplePicture", "samplePicture" + i + ".jpeg"));
        }

        for (int i = 5; i < 7; i++) {
            pictureService.create("title" + i, "content" + i, PictureType.TRAVLE,
                    pictureMember.getId(), new Img("samplePicture", "samplePicture" + i + ".jpg"));
        }

        Picture picture = pictureService.findByMember(pictureMember.getId()).get(0);

        //likes
        likesService.oneClick(likeFollowMembr1.getId(), picture.getId());
        likesService.oneClick(likeFollowMembr2.getId(), picture.getId());

        //follow
        followService.oneClick(likeFollowMembr1.getId(), pictureMember.getId());
        followService.oneClick(likeFollowMembr2.getId(), pictureMember.getId());
    }

}
