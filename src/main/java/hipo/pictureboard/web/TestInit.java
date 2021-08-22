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
import java.util.List;
import java.util.Random;

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

        Member Member1 = memberService.findByNickName("hipo1").get(0);
        Member Member2 = memberService.findByNickName("hipo2").get(0);
        Member Member3 = memberService.findByNickName("hipo3").get(0);

        //그림 생성
        for (int i = 1; i < 18; i++) {
            pictureService.create("title" + i, "content" + i, PictureType.SCENERY,
                    Member1.getId(), new Img("samplePicture", "samplePicture" + i + ".jpeg"));
        }

        for (int i = 18; i < 24; i++) {
            pictureService.create("title" + i, "content" + i, PictureType.TRAVLE,
                    Member2.getId(), new Img("samplePicture", "samplePicture" + i + ".jpg"));
        }

        for (int i = 24; i < 30; i++) {
            pictureService.create("title" + i, "content" + i, PictureType.PEOPLE,
                    Member3.getId(), new Img("samplePicture", "samplePicture" + i + ".webp"));
        }


        //likes

        Random random = new Random();
        List<Picture> pictures = pictureService.findAll();
        for (int i = 4; i < 31; i++) {
            Member member = memberService.findByNickName("hipo" + i).get(0);
            for (int j = 0; j < 20; j++) {
                likesService.oneClick(member.getId(), pictures.get((random.nextInt(28) + 1)).getId());
            }
        }

        //follow
        followService.oneClick(Member1.getId(), Member2.getId());
        followService.oneClick(Member1.getId(), Member3.getId());
        followService.oneClick(Member2.getId(), Member1.getId());
        followService.oneClick(Member2.getId(), Member3.getId());
        followService.oneClick(Member3.getId(), Member1.getId());
        followService.oneClick(Member3.getId(), Member2.getId());

        for (int i = 4; i < 31; i++) {
            Member member = memberService.findByNickName("hipo" + i).get(0);
            for (int j = 0; j < 20; j++) {
                Member followedMember = memberService.findByNickName("hipo" + (random.nextInt(28) + 1)).get(0);
                followService.oneClick(member.getId(), followedMember.getId());
            }
        }
    }

}
