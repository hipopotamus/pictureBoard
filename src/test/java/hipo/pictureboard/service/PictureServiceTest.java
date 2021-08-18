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

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PictureServiceTest {

    @Autowired PictureService pictureService;
    @Autowired MemberService memberService;
    @Autowired FollowService followService;

    public void makePictures() {
        Member member = memberService.create("member1", "password1", "nickName1", new Img("fileName", "storeFileName"));
        Member member2 = memberService.create("member2", "password2", "nickName2", new Img("fileName2", "storeFileName2"));
        memberService.create("member3", "password3", "nickName3", new Img("fileName3", "storeFileName3"));
        for (int i = 0; i < 100; i++) {
            String title = "title" + i;
            String content = "content" + i;
            PictureType pictureType = PictureType.PEOPLE;
            Long memberId = member.getId();
            Img pictureImg = new Img("fileName", "storeFileName");
            Picture picture = pictureService.create(title, content, pictureType, memberId, pictureImg);
            picture.setLikeCount(i);
        }

        for (int i = 100; i < 150; i++) {
            String title = "title" + i;
            String content = "content" + i;
            PictureType pictureType = PictureType.TRAVLE;
            Long memberId = member2.getId();
            Img pictureImg = new Img("fileName", "storeFileName");
            Picture picture = pictureService.create(title, content, pictureType, memberId, pictureImg);
            picture.setLikeCount(i);
        }
    }

    @Test
//    @Rollback(value = false)
    public void 사진_전체_조회() {
        makePictures();
        List<Picture> pictures = pictureService.findAll();
        System.out.println("조회된 사진의 총 사이즈는 " + pictures.size() + " 입니다.");
        System.out.println("조회된 사진의 총 사이즈는 " + pictureService.AllPictureSize() + " 입니다(함수).");
    }

    @Test
//    @Rollback(value = false)
    public void 사진_페이지_조회() {
        makePictures();

        List<Picture> pictures = pictureService.AllByPage(1);

        pictures.stream()
                .forEach(p -> System.out.println(p.getId()));
    }

    @Test
    @Rollback(value = false)
    public void 사진_멤버_조회() {
        makePictures();
        Member member = memberService.findByLoginId("member1").get(0);
        List<Picture> pictures = pictureService.MemberByPage(member.getId(), 1);
        pictures.stream()
                .forEach(p -> System.out.println(p.getTitle()));
    }

    @Test
    @Rollback(value = false)
    public void 사진_타입_조회() {
        makePictures();
        List<Picture> pictures = pictureService.PictureTypeByPage(PictureType.TRAVLE, 4);
        pictures.stream()
                .forEach(p -> System.out.println(p.getTitle()));
    }

    @Test
//    @Rollback(value = false)
    public void 사진_팔로우_조회() {
        makePictures();
        List<Member> members1 = memberService.findByLoginId("member1");
        Member member1 = memberService.findByLoginId("member1").get(0);
        Member member2 = memberService.findByLoginId("member2").get(0);
        Member member3 = memberService.findByLoginId("member3").get(0);
        Follow follow1 = followService.create(member3.getId(), member1.getId());
        Follow follow2 = followService.create(member3.getId(), member2.getId());
        List<Picture> pictures = pictureService.recommendByFollow(member3.getId());
        System.out.println("조회된 사진의 총 사이즈는 " + pictures.size() + " 입니다.");
        pictures.stream()
                .forEach(p -> System.out.println(p.getId()));
    }

    @Test
    public void 사진_좋아요_조회() {
        makePictures();
        List<Picture> all = pictureService.findAll();
        all.get(50).setLikeCount(1000);
        List<Picture> pictures = pictureService.rankByLikes();
        System.out.println("첫 번째 사진의 좋아요 수는 " + pictures.get(0).getLikeCount() + " 입니다." );
    }

    @Test
    public void 사진_타이틀_검색명_조회() {
        makePictures();
        List<Picture> all = pictureService.findAll();
        all.get(50).setTitle("testTitle1");
        all.get(51).setTitle("testTitle2");
        all.get(90).setTitle("Titletest1");
        List<Picture> pictures = pictureService.searchByTitle("tes");
        pictures.stream()
                .forEach(p -> System.out.println("검색된 사진의 타이틀 = " + p.getTitle()));
    }

    @Test
    public void 사진_타이틀_검색명_페이지_조회() {
        makePictures();
        List<Picture> all = pictureService.findAll();
        all.get(50).setTitle("testTitle1");
        all.get(51).setTitle("testTitle2");
        all.get(90).setTitle("Titletest1");
        List<Picture> pictures = pictureService.TitleByPage("tes", 1);
        pictures.stream()
                .forEach(p -> System.out.println("검색된 사진의 타이틀 = " + p.getTitle()));
    }

}