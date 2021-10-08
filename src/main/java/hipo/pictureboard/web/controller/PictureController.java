package hipo.pictureboard.web.controller;

import hipo.pictureboard.domain.Member;
import hipo.pictureboard.domain.Picture;
import hipo.pictureboard.domain.PictureType;
import hipo.pictureboard.service.*;
import hipo.pictureboard.web.argumentresolver.Login;
import hipo.pictureboard.web.form.NewPictureForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PictureController {

    private final MemberService memberService;
    private final PictureService pictureService;
    private final FileService fileService;
    private final LikesService likesService;
    private final FollowService followService;
    private final LoginService loginService;

    @Value("${file.picture}")
    private String filePicture;

    @ModelAttribute("pictureType")
    public PictureType[] pictureTypes() {
        return PictureType.values();
    }

    @GetMapping("/picture/add")
    public String createPicture(@Login Member member, @ModelAttribute("pictureForm") NewPictureForm pictureForm, Model model) {
        Member loginMember = memberService.findByOne(member.getId());
        model.addAttribute("loginMember", loginMember);
        return "/pictures/newPictureForm";
    }

    @PostMapping("/picture/add")
    public String addPicture(@Login Member loginMember, @Validated @ModelAttribute("pictureForm") NewPictureForm pictureForm,
                             BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) throws IOException {
        model.addAttribute("loginMember", loginMember);

        if (pictureForm.getPictureFile().getOriginalFilename().isBlank()) {
            bindingResult.rejectValue("pictureFile", "Empty");
        }

        if (bindingResult.hasErrors()) {
            return "/pictures/newPictureForm";
        }

        Picture createdPicture = pictureService.create(pictureForm.getTitle(), pictureForm.getContent(), pictureForm.getPictureType(),
                loginMember.getId(), fileService.storeFile(pictureForm.getPictureFile(), filePicture));
        redirectAttributes.addAttribute("pictureId", createdPicture.getId());

        return "redirect:/picture/{pictureId}";
    }

    @GetMapping("/picture/{pictureId}")
    public String picture(@Login Member member, @PathVariable Long pictureId, Model model) {
        Member loginMember = memberService.findByOne(member.getId());
        model.addAttribute("loginMember", loginMember);

        Picture picture = pictureService.findByOne(pictureId);
        boolean likeCheck = likesService.likesCheck(loginMember.getId(), pictureId);
        boolean followCheck = followService.followCheck(loginMember.getId(), picture.getMember().getId());
        boolean selfFollowCheck = loginService.checkMyPicture(loginMember.getId(), picture.getMember().getId());
        model.addAttribute("picture", picture);
        model.addAttribute("likeCheck", likeCheck);
        model.addAttribute("followCheck", followCheck);
        model.addAttribute("selfFollowCheck", selfFollowCheck);

        return "/pictures/picture";
    }

    @GetMapping("picture/like/{pictureId}")
    public String clickLikes(@Login Member loginMember, @PathVariable Long pictureId) {
        likesService.onClick(loginMember.getId(), pictureId);
        return "redirect:/picture/{pictureId}";
    }

    @GetMapping("picture/follow/{pictureId}")
    public String clickFollow(@Login Member loginMember, @PathVariable Long pictureId) {
        Long followedMemberId = pictureService.findByOne(pictureId).getMember().getId();
        followService.onClick(loginMember.getId(), followedMemberId);
        return "redirect:/picture/{pictureId}";
    }
}
