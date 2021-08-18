package hipo.pictureboard.service;

import hipo.pictureboard.domain.*;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class LikesServiceTest {

    @Autowired MemberService memberService;
    @Autowired PictureService pictureService;
    @Autowired LikesService likesService;

    private void makeLikes() {
        Member member1 = memberService.create("member1", "password1", "nickName1",
                new Img("fileName1", "storeName1"));
        Picture picture1 = pictureService.create("title1", "content1", PictureType.PEOPLE,
                member1.getId(), new Img("fileName2", "storeFileName2"));

        Likes likes = likesService.create(member1.getId(), picture1.getId());
        picture1.addLikeCount();
    }

    @Test
    public void 좋아요_상태_확인_테스트() {
        makeLikes();

        Member member1 = memberService.create("member2", "password2", "nickName2",
                new Img("fileName3", "storeName3"));
        Member member2 = memberService.findByLoginId("member1").get(0);
        Picture picture = pictureService.findByMember(member2.getId()).get(0);
        likesService.oneClick(member2.getId(), picture.getId());
        likesService.oneClick(member2.getId(), picture.getId());
        likesService.oneClick(member2.getId(), picture.getId());
        likesService.oneClick(member2.getId(), picture.getId());
        boolean likeStatus = likesService.findByOneMember(member2.getId(), picture.getId());
        System.out.println("좋아요 상태는 = " + likeStatus);
        Assertions.assertThat(likeStatus).isEqualTo(true);
    }

    @Test
//    @Rollback(value = false)
    public void 좋아요_클릭_생성_테스트() {
        makeLikes();

        Member member2 = memberService.create("member2", "password2", "nickName2",
                new Img("fileName3", "storeName3"));
        Member member1 = memberService.findByLoginId("member1").get(0);
        Picture picture = pictureService.findByMember(member1.getId()).get(0);
        Likes likes = likesService.oneClick(member2.getId(), picture.getId());
        likesService.oneClick(member2.getId(), picture.getId());
        likesService.oneClick(member2.getId(), picture.getId());
        likesService.oneClick(member2.getId(), picture.getId());
        boolean likeStatus = likesService.findByOneMember(member2.getId(), picture.getId());
        System.out.println("좋아요 상태는 = " + likeStatus);
        Assertions.assertThat(likeStatus).isEqualTo(false);
    }

}