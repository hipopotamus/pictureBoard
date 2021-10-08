package hipo.pictureboard.web.controller;

import hipo.pictureboard.domain.Member;
import hipo.pictureboard.domain.Picture;
import hipo.pictureboard.domain.PictureType;
import hipo.pictureboard.service.MemberService;
import hipo.pictureboard.service.PageService;
import hipo.pictureboard.service.PictureService;
import hipo.pictureboard.web.argumentresolver.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final MemberService memberService;
    private final PictureService pictureService;
    private final PageService pageService;

    @GetMapping("/boards/main")
    public String mainBoard(@Login Member member, Model model) {
        Member loginMember = memberService.findByOne(member.getId());
        List<Picture> pictures = pictureService.findByAll();
        List<Picture> rankedPictures = pictureService.findByAllOrderedLikes();
        List<Picture> followPictures = pictureService.recommendPictureByFollow(loginMember.getId());

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("pictures", pictures);
        model.addAttribute("rankedPictures", rankedPictures);
        model.addAttribute("followPictures", followPictures);

        return "/boards/main";
    }

    @GetMapping("/boards/picture/{page}")
    public String pictureBoard(@Login Member member, @PathVariable int page, Model model) {
        Member loginMember = memberService.findByOne(member.getId());
        model.addAttribute("loginMember", loginMember);
        List<Picture> pictures = pictureService.PagingByAll(page);
        int totalPictureSize = pictureService.SizeByAll();
        Map<String, Boolean> checkPage = pageService.checkPage(page, totalPictureSize);
        int[] pageNumber = pageService.makePageNumbers(page, totalPictureSize);
        model.addAttribute("checkPage", checkPage);
        model.addAttribute("pictures", pictures);
        model.addAttribute("pageNumber", pageNumber);
        return "/boards/allPicture";
    }

    @GetMapping("/boards/picture/people/{page}")
    public String peoplePictureBoard(@Login Member member, @PathVariable int page, Model model) {
        Member loginMember = memberService.findByOne(member.getId());
        model.addAttribute("loginMember", loginMember);
        List<Picture> pictures = pictureService.PagingByPictureType(PictureType.PEOPLE, page);
        int totalPictureSize = pictureService.PictureTypeSize(PictureType.PEOPLE);
        Map<String, Boolean> checkPage = pageService.checkPage(page, totalPictureSize);
        int[] pageNumber = pageService.makePageNumbers(page, totalPictureSize);
        model.addAttribute("checkPage", checkPage);
        model.addAttribute("pictures", pictures);
        model.addAttribute("pageNumber", pageNumber);
        return "/boards/people";
    }

    @GetMapping("/boards/picture/scenery/{page}")
    public String sceneryPictureBoard(@Login Member member, @PathVariable int page, Model model) {
        Member loginMember = memberService.findByOne(member.getId());
        model.addAttribute("loginMember", loginMember);
        List<Picture> pictures = pictureService.PagingByPictureType(PictureType.SCENERY, page);
        int totalPictureSize = pictureService.PictureTypeSize(PictureType.SCENERY);
        Map<String, Boolean> checkPage = pageService.checkPage(page, totalPictureSize);
        int[] pageNumber = pageService.makePageNumbers(page, totalPictureSize);
        model.addAttribute("checkPage", checkPage);
        model.addAttribute("pictures", pictures);
        model.addAttribute("pageNumber", pageNumber);
        return "/boards/scenery";
    }

    @GetMapping("/boards/picture/travel/{page}")
    public String travelPictureBoard(@Login Member member, @PathVariable int page, Model model) {
        Member loginMember = memberService.findByOne(member.getId());
        model.addAttribute("loginMember", loginMember);
        List<Picture> pictures = pictureService.PagingByPictureType(PictureType.TRAVEL, page);
        int totalPictureSize = pictureService.PictureTypeSize(PictureType.TRAVEL);
        Map<String, Boolean> checkPage = pageService.checkPage(page, totalPictureSize);
        int[] pageNumber = pageService.makePageNumbers(page, totalPictureSize);
        model.addAttribute("checkPage", checkPage);
        model.addAttribute("pictures", pictures);
        model.addAttribute("pageNumber", pageNumber);
        return "/boards/travel";
    }

    @GetMapping("/boards/picture/search/{page}")
    public String searchByTitleBoard(@Login Member member, @RequestParam("search") String search, @PathVariable int page, Model model) {
        Member loginMember = memberService.findByOne(member.getId());
        model.addAttribute("loginMember", loginMember);
        List<Picture> pictures = pictureService.PagingBySearchedTitle(search, page);
        int totalPictureSize = pictureService.SearchedTitleSize(search);
        Map<String, Boolean> checkPage = pageService.checkPage(page, totalPictureSize);
        int[] pageNumber = pageService.makePageNumbers(page, totalPictureSize);
        model.addAttribute("search", search);
        model.addAttribute("checkPage", checkPage);
        model.addAttribute("searchTitle", search);
        model.addAttribute("pictures", pictures);
        model.addAttribute("pageNumber", pageNumber);
        return "/boards/searchByTitle";
    }
}
