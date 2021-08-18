package hipo.pictureboard.web.controller;

import hipo.pictureboard.domain.Member;
import hipo.pictureboard.domain.PictureType;
import hipo.pictureboard.service.PictureService;
import hipo.pictureboard.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequiredArgsConstructor
public class PictureController {

    private final PictureService pictureService;

    @ModelAttribute("pictureType")
    public PictureType[] pictureTypes() {
        return PictureType.values();
    }

    @GetMapping("/pictuer/add")
    public String createPicture(@Login Member loginMember) {
        return "/pictures/newPictureForm";
    }
}
