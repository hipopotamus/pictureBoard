package hipo.pictureboard.service;

import hipo.pictureboard.domain.Img;
import hipo.pictureboard.domain.Member;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Value("${file.profile}")
    private String fileProfile;


    private void makeMembers() {
        for (int i = 0; i < 100; i++) {
            String loginId = "loginId" + i;
            String password = "password" + i;
            String nickName = "nickName" + i;
            Img profileFile = new Img("fileName" + i, "storeFileName" + i);
            memberService.create(loginId, password, nickName, profileFile);
        }
    }

    @Test
    @Rollback(value = false)
    public void 회원등록() throws Exception {
        makeMembers();

        String loginId = "qkqntjddn";
        String password = "1234";
        String nickName = "hipo";
        Img profileFile = new Img("fileName", "storeFileName");

        Member member = memberService.create(loginId, password, nickName, profileFile);

        Assertions.assertEquals(member, memberService.findOne(member.getId()));
    }

    @Test(expected = IllegalStateException.class)
    @Rollback
//    @Rollback(value = false)
    public void 회원아이디_중복_가입() throws Exception {
        makeMembers();

        String loginId = "qkqntjddn";
        String password = "1234";
        String nickName = "hipo";
//        Img profileFile = new Img("fileName", "storeFileName");
        Img profileFile = null;

        String loginId2 = "qkqntjddn";
        String password2 = "1234";
        String nickName2 = "hipo2";

        Member member = memberService.create(loginId, password, nickName, profileFile);
        Member member2 = memberService.create(loginId2, password2, nickName2, profileFile);
    }

    @Test(expected = IllegalStateException.class)
    @Rollback
//    @Rollback(value = false)
    public void 회원닉네임_중복_가입() throws Exception {
        makeMembers();

        String loginId = "qkqntjddn";
        String password = "1234";
        String nickName = "hipo";
//        Img profileFile = new Img("fileName", "storeFileName");
        Img profileFile = null;

        String loginId2 = "qkqntjddn1";
        String password2 = "1234";
        String nickName2 = "hipo";

        Member member = memberService.create(loginId, password, nickName, profileFile);
        Member member2 = memberService.create(loginId2, password2, nickName2, profileFile);
    }
}