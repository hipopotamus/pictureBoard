package hipo.pictureboard.service;

import hipo.pictureboard.domain.Follow;
import hipo.pictureboard.domain.Img;
import hipo.pictureboard.domain.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class FollowServiceTest {

    @Autowired MemberService memberService;
    @Autowired FollowService followService;

    public void makeFollow() {
        Member followMember1 = memberService.create("followMember1", "Fpassword1",
                "FnickName1", new Img("FfileName1", "FstoreFileName1"));

        Member followMember2 = memberService.create("followMember2", "Fpassword2",
                "FnickName2", new Img("FfileName2", "FstoreFileName2"));

        memberService.create("followMember3", "Fpassword3",
                "FnickName3", new Img("FfileName3", "FstoreFileName3"));

        for (int i = 1; i < 51; i++) {
            Member followedMember = memberService.create("followedMember" + i, "password" + i,
                    "nickName" + i, new Img("fileName" + i, "storeFileName" + i));
            followService.create(followMember1.getId(), followedMember.getId());
            followMember1.addFollowing();
            followedMember.addFollower();
        }

        for (int i = 51; i < 71; i++) {
            Member followedMember = memberService.create("followedMember" + i, "password" + i,
                    "nickName" + i, new Img("fileName" + i, "storeFileName" + i));
            followService.create(followMember2.getId(), followedMember.getId());
            followMember2.addFollowing();
            followedMember.addFollower();
        }
    }

    @Test
    public void 팔로우중_멤버_조회() {
        makeFollow();
        Member member1 = memberService.findByLoginId("followMember1").get(0);
        Member member2 = memberService.findByLoginId("followMember2").get(0);
        Member member3 = memberService.findByLoginId("followMember3").get(0);
        Member member4 = memberService.findByLoginId("followedMember1").get(0);

        List<Follow> follows1 = followService.findByFollowMember(member1.getId());
        List<Follow> follows2 = followService.findByFollowMember(member2.getId());
        List<Follow> follows3 = followService.findByFollowMember(member3.getId());


        assertThat(follows1.size()).isEqualTo(50);
        assertThat(follows2.size()).isEqualTo(20);
        assertThat(follows3.size()).isEqualTo(0);

        assertThat(member1.getFollowing()).isEqualTo(50);
    }

    @Test
    public void 팔로우_클릭_테스트() {
        makeFollow();
        Member member1 = memberService.findByLoginId("followMember1").get(0);
        Member member2 = memberService.findByLoginId("followMember2").get(0);
        Member member3 = memberService.findByLoginId("followMember3").get(0);
        Member member4 = memberService.findByLoginId("followedMember1").get(0);

        followService.onClick(member1.getId(), member4.getId());
        followService.onClick(member1.getId(), member4.getId());
        boolean status1 = followService.followCheck(member1.getId(), member4.getId());

        followService.onClick(member3.getId(), member4.getId());
        followService.onClick(member3.getId(), member4.getId());
        boolean status2 = followService.followCheck(member3.getId(), member4.getId());

        assertThat(status1).isEqualTo(true);
        assertThat(status2).isEqualTo(false);

    }


}