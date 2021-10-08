package hipo.pictureboard.service;

import hipo.pictureboard.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PictureServiceTest {

    @Autowired PictureService pictureService;
    @Autowired MemberService memberService;
    @Autowired FollowService followService;
    @Autowired EntityManager em;

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
            Picture picture = pictureService.create(title, content, pictureType, memberId, pictureImg);}

        for (int i = 100; i < 150; i++) {
            String title = "title" + i;
            String content = "content" + i;
            PictureType pictureType = PictureType.TRAVEL;
            Long memberId = member2.getId();
            Img pictureImg = new Img("fileName", "storeFileName");
            Picture picture = pictureService.create(title, content, pictureType, memberId, pictureImg);
        }
    }

    @Test
    @Rollback(value = false)
    public void 조인정렬테스트() {
        makePictures();
        TypedQuery<Member> query = em.createQuery("select m from Picture p left join p.member m on m.profilePicture.fileName = 'sample1'", Member.class);
        List<Member> resultList = query.getResultList();
//
        System.out.println("size = " + resultList.size() );
        System.out.println("============================");

        for (Member member : resultList) {
            System.out.println("member = " + member.getNickName());
        }

//        for (Object[] objects : resultList) {
//            Member member = (Member) objects[0];
//            Picture picture = (Picture) objects[1];
//            System.out.println("member.getNickName() = " + member.getNickName());
//            System.out.println("picture = " + picture.getTitle());
//            System.out.println("==============================================");
//        }
    }

    @Test
//    @Rollback(value = false)
    public void 사진_전체_조회() {
        makePictures();
        List<Picture> pictures = pictureService.findByAll();
        System.out.println("조회된 사진의 총 사이즈는 " + pictures.size() + " 입니다.");
        System.out.println("조회된 사진의 총 사이즈는 " + pictureService.SizeByAll() + " 입니다(함수).");
    }

    @Test
//    @Rollback(value = false)
    public void 사진_페이지_조회() {
        makePictures();

        List<Picture> pictures = pictureService.PagingByAll(1);

        pictures.stream()
                .forEach(p -> System.out.println(p.getId()));
    }


    @Test
    @Rollback(value = false)
    public void 사진_타입_조회() {
        makePictures();
        List<Picture> pictures = pictureService.PagingByPictureType(PictureType.TRAVEL, 4);
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
        List<Picture> pictures = pictureService.recommendPictureByFollow(member3.getId());
        System.out.println("조회된 사진의 총 사이즈는 " + pictures.size() + " 입니다.");
        pictures.stream()
                .forEach(p -> System.out.println(p.getId()));
    }




}