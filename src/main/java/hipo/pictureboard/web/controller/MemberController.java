package hipo.pictureboard.web.controller;

import hipo.pictureboard.service.FileService;
import hipo.pictureboard.service.MemberService;
import hipo.pictureboard.web.form.NewMemberForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final FileService fileService;

    @Value("${file.profile}")
    private String fileProfile;

    @GetMapping("/members/new")
    public String newMemberForm(@ModelAttribute("form") NewMemberForm form) {
        return "/members/newMemberForm";
    }

    @PostMapping("/members/new")
    public String createMember(@Validated @ModelAttribute("form") NewMemberForm form, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return "/members/newMemberForm";
        }

        if (form.getProfilePicture().getOriginalFilename().isBlank()) {
            bindingResult.rejectValue("profilePicture", "Empty", "프로필 사진을 등록해주세요");
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
}
