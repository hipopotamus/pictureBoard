package hipo.pictureboard.web.controller;

import hipo.pictureboard.domain.Member;
import hipo.pictureboard.domain.Picture;
import hipo.pictureboard.service.FileService;
import hipo.pictureboard.service.FollowService;
import hipo.pictureboard.service.MemberService;
import hipo.pictureboard.service.PictureService;
import hipo.pictureboard.web.argumentresolver.Login;
import hipo.pictureboard.web.dto.FollowPictureDTO;
import hipo.pictureboard.web.form.NewMemberForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final PictureService pictureService;
    private final FollowService followService;
    private final FileService fileService;

    @Value("${file.profile}")
    private String fileProfile;

    @GetMapping("/member/new")
    public String newMemberForm(@ModelAttribute("form") NewMemberForm form) {
        return "/members/newMemberForm";
    }

    @PostMapping("/member/new")
    public String createMember(@Validated @ModelAttribute("form") NewMemberForm form, BindingResult bindingResult) throws IOException {

        if (form.getProfilePicture().getOriginalFilename().isBlank()) {
            bindingResult.rejectValue("profilePicture", "Empty");
        }

        if (bindingResult.hasErrors()) {
            return "/members/newMemberForm";
        }

        try {
            memberService.create(form.getLoginId(), form.getPassword(), form.getNickName(), fileService.storeFile(form.getProfilePicture(), fileProfile));
        } catch (IllegalStateException e) {
            bindingResult.reject("duplication", "아이디 또는 닉네임이 중복되었습니다.");
            return "/members/newMemberForm";
        }

        return "redirect:/login";
    }

    @GetMapping("/member/room")
    public String myRoom(@Login Member member, Model model) {
        Member loginMember = memberService.findOne(member.getId());
        model.addAttribute("loginMember", loginMember);
        List<Picture> pictures = pictureService.findByMember(loginMember.getId());
        model.addAttribute("pictures", pictures);
        return "/members/myRoom";
    }

    @GetMapping("/member/room/pictures")
    public String myRoomPictures(@Login Member member, Model model) {
        Member loginMember = memberService.findOne(member.getId());
        model.addAttribute("loginMember", loginMember);
        List<Picture> pictures = pictureService.findByMember(loginMember.getId());
        model.addAttribute("pictures", pictures);
        return "/members/myRoomPictures";
    }

    @GetMapping("/member/room/follow")
    public String myRoomFollow(@Login Member member, Model model) {
        Member loginMember = memberService.findOne(member.getId());
        model.addAttribute("loginMember", loginMember);
        List<FollowPictureDTO> followAndPictures = pictureService.getFollowAndPictures(loginMember.getId());
        model.addAttribute("followAndPictures", followAndPictures);
        return "/members/myRoomFollow";
    }

    @GetMapping("/member/otherRoom/{memberId}")
    public String otherRoom(@Login Member member, @PathVariable Long memberId,Model model) {
        Member loginMember = memberService.findOne(member.getId());
        Member otherMember = memberService.findOne(memberId);

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("otherMember", otherMember);
        List<Picture> picturs = pictureService.findByMember(memberId);
        model.addAttribute("pictures", picturs);
        return "/members/otherRoom";
    }

    @GetMapping("/member/otherRoom/pictures/{memberId}")
    public String otherRoomPictures(@Login Member member, @PathVariable Long memberId, Model model) {
        Member loginMember = memberService.findOne(member.getId());
        Member otherMember = memberService.findOne(memberId);
        model.addAttribute("loginMember", loginMember);
        model.addAttribute("otherMember", otherMember);
        List<Picture> pictures = pictureService.findByMember(memberId);
        model.addAttribute("pictures", pictures);
        return "/members/otherRoomPictures";
    }

    @GetMapping("/member/otherRoom/follow/{memberId}")
    public String otherRoomFollow(@Login Member member, @PathVariable Long memberId, Model model) {
        Member loginMember = memberService.findOne(member.getId());
        Member otherMember = memberService.findOne(memberId);
        model.addAttribute("loginMember", loginMember);
        model.addAttribute("otherMember", otherMember);
        List<FollowPictureDTO> followAndPictures = pictureService.getOtherFollowAndPictures(memberId, loginMember.getId());
        model.addAttribute("followAndPictures", followAndPictures);
        return "/members/otherRoomFollow";
    }

    @GetMapping("member/room/followOneClick/{followedMemberId}")
    public String clickMyRoomFollow(@Login Member loginMember, @PathVariable Long followedMemberId) {
        followService.oneClick(loginMember.getId(), followedMemberId);
        return "redirect:/member/room/follow";
    }

    @GetMapping("member/otherRoom/followOneClick/{followedMemberId}")
    public String clickOtherRoomFollow(@Login Member loginMember, @PathVariable Long followedMemberId, @RequestParam("otherMemberId") Long otherMemberId, RedirectAttributes redirectAttributes) {
        followService.oneClick(loginMember.getId(), followedMemberId);
        redirectAttributes.addAttribute("otherMemberId", otherMemberId);
        return "redirect:/member/otherRoom/follow/{otherMemberId}";
    }
}
