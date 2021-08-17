package hipo.pictureboard.service;

import hipo.pictureboard.domain.Img;
import hipo.pictureboard.domain.Member;
import hipo.pictureboard.domain.Picture;
import hipo.pictureboard.domain.PictureType;
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

    public void makePictures() {
        Member member = memberService.create("member1", "password1", "nickName1", new Img("fileName", "storeFileName"));
        Member member2 = memberService.create("member2", "password2", "nickName2", new Img("fileName2", "storeFileName2"));
        for (int i = 0; i < 100; i++) {
            String title = "title" + i;
            String content = "content" + i;
            PictureType pictureType = PictureType.PEOPLE;
            Long memberId = member.getId();
            Img pictureImg = new Img("fileName", "storeFileName");
            Picture picture = pictureService.create(title, content, pictureType, memberId, pictureImg);
            picture.setLikeCount(i);
        }
    }

    @Test
    @Rollback(value = false)
    public void 사진_전체_조회() {
        makePictures();
        List<Picture> pictures = pictureService.findAll();
        System.out.println("사진의 총 사이즈는 " + pictures.size() + " 입니다.");
        Assertions.assertThat(pictures.size()).isEqualTo(100);
    }

    @Test
//    @Rollback(value = false)
    public void 사진_팔로우_조회() {
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
        all.get(50).setTitle("testTitle");
        List<Picture> pictures = pictureService.searchByTitle("tes");
        pictures.stream()
                .forEach(p -> System.out.println("검색된 사진의 타이틀 = " + p.getTitle()));
        System.out.println("검색된 사진들 = " + pictures);
    }

}