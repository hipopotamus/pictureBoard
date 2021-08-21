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
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final MemberService memberService;
    private final PictureService pictureService;
    private final PageService pageService;

    @GetMapping("/boards/main")
    public String mainBoard(@Login Member member, Model model) {
        Member loginMember = memberService.findOne(member.getId());
        List<Picture> pictures = pictureService.findAll();
        List<Picture> rankedPictures = pictureService.rankByLikes();
        List<Picture> followPictures = pictureService.recommendByFollow(loginMember.getId());

        model.addAttribute("loginMember", loginMember);
        model.addAttribute("pictures", pictures);
        model.addAttribute("rankedPictures", rankedPictures);
        model.addAttribute("followPictures", followPictures);

        return "/boards/main";
    }

    @GetMapping("/boards/picture/{page}")
    public String pictureBoard(@Login Member member, @PathVariable int page, Model model) {
        Member loginMember = memberService.findOne(member.getId());
        model.addAttribute("loginMember", loginMember);
        List<Picture> pictures = pictureService.AllByPage(page);
        int totalPictureSize = pictureService.AllPictureSize();
        int[] pageNumber = pageService.makePageNumber(page, totalPictureSize);
        boolean lastPageCheck = pageService.checkLastPage(page, totalPictureSize);
        model.addAttribute("pictures", pictures);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("lastPageCheck", lastPageCheck);
        return "/boards/allPicture";
    }

    @GetMapping("/boards/picture/people/{page}")
    public String peoplePictureBoard(@Login Member member, @PathVariable int page, Model model) {
        Member loginMember = memberService.findOne(member.getId());
        model.addAttribute("loginMember", loginMember);
        List<Picture> pictures = pictureService.PictureTypeByPage(PictureType.PEOPLE, page);
        int totalPictureSize = pictureService.PictureTypePictureSize(PictureType.PEOPLE);
        int[] pageNumber = pageService.makePageNumber(page, totalPictureSize);
        boolean lastPageCheck = pageService.checkLastPage(page, totalPictureSize);
        model.addAttribute("pictures", pictures);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("lastPageCheck", lastPageCheck);
        return "/boards/people";
    }

    @GetMapping("/boards/picture/scenery/{page}")
    public String sceneryPictureBoard(@Login Member member, @PathVariable int page, Model model) {
        Member loginMember = memberService.findOne(member.getId());
        model.addAttribute("loginMember", loginMember);
        List<Picture> pictures = pictureService.PictureTypeByPage(PictureType.SCENERY, page);
        int totalPictureSize = pictureService.PictureTypePictureSize(PictureType.SCENERY);
        int[] pageNumber = pageService.makePageNumber(page, totalPictureSize);
        boolean lastPageCheck = pageService.checkLastPage(page, totalPictureSize);
        model.addAttribute("pictures", pictures);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("lastPageCheck", lastPageCheck);
        return "/boards/scenery";
    }

    @GetMapping("/boards/picture/travel/{page}")
    public String travelPictureBoard(@Login Member member, @PathVariable int page, Model model) {
        Member loginMember = memberService.findOne(member.getId());
        model.addAttribute("loginMember", loginMember);
        List<Picture> pictures = pictureService.PictureTypeByPage(PictureType.TRAVLE, page);
        int totalPictureSize = pictureService.PictureTypePictureSize(PictureType.TRAVLE);
        int[] pageNumber = pageService.makePageNumber(page, totalPictureSize);
        boolean lastPageCheck = pageService.checkLastPage(page, totalPictureSize);
        model.addAttribute("pictures", pictures);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("lastPageCheck", lastPageCheck);
        return "/boards/travel";
    }

    @GetMapping("/boards/picture/search/{page}")
    public String searchByTitleBoard(@Login Member member, @RequestParam("search") String search, @PathVariable int page, Model model) {
        Member loginMember = memberService.findOne(member.getId());
        model.addAttribute("loginMember", loginMember);
        List<Picture> pictures = pictureService.searchByTitle(search);
        int totalPictureSize = pictureService.TitlePictureSize(search);
        int[] pageNumber = pageService.makePageNumber(page, totalPictureSize);
        boolean lastPageCheck = pageService.checkLastPage(page, totalPictureSize);
        model.addAttribute("searchTitle", search);
        model.addAttribute("pictures", pictures);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("lastPageCheck", lastPageCheck);
        return "/boards/searchByTitle";
    }
}
