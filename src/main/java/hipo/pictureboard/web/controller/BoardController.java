package hipo.pictureboard.web.controller;

import hipo.pictureboard.domain.Member;
import hipo.pictureboard.domain.Picture;
import hipo.pictureboard.service.PictureService;
import hipo.pictureboard.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final PictureService pictureService;

    @GetMapping("/boards/main")
    public String mainBoard(@Login Member loginMember, Model model) {
        List<Picture> pictures = pictureService.findAll();
        log.info("프로필 저장 아이디 = {}", loginMember.getProfilePicture().getStoreFileName());


        model.addAttribute("loginMember", loginMember);
        model.addAttribute("pictures", pictures);

        return "/boards/main";
    }
}
