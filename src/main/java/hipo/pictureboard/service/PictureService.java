package hipo.pictureboard.service;

import hipo.pictureboard.domain.*;
import hipo.pictureboard.repository.FollowRepository;
import hipo.pictureboard.repository.MemberRepository;
import hipo.pictureboard.repository.PictureRepository;
import hipo.pictureboard.web.dto.FollowPictureDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PictureService {

    private final MemberRepository memberRepository;
    private final PictureRepository pictureRepository;
    private final FollowRepository followRepository;
    private final FollowService followService;

    @Transactional
    public Picture create(String title, String content, PictureType pictureType,
                          Long memberId, Img pictureImg) {
        Member member = memberRepository.findOne(memberId);
        Picture picture = new Picture(title, content, pictureType, member, pictureImg);
        pictureRepository.save(picture);
        return picture;
    }

    public Picture findByOne(Long pictureId) {
        return pictureRepository.findOne(pictureId);
    }

    //전체 조회 관련
    public List<Picture> findByAll() {
        return pictureRepository.findByAll();
    }

    public List<Picture> PagingByAll(int page) {
        return pictureRepository.PagingByAll(page);
    }

    public int SizeByAll() {
        return pictureRepository.findByAll().size();
    }

    //멤버 조회 관련
    public List<Picture> findByMember(Long memberId) {
        Member member = memberRepository.findOne(memberId);
        return pictureRepository.findByMember(member);
    }

    //사진 타입 조회 관련
    public List<Picture> PagingByPictureType(PictureType pictureType, int page) {
        return pictureRepository.PagingByPictureType(pictureType, page);
    }

    public int PictureTypeSize(PictureType pictureType) {
        return pictureRepository.findByPictureType(pictureType).size();
    }

    //검색 조회 관련
    public List<Picture> findBySearchedTitle(String title) {
        return pictureRepository.findBySearchedTitle(title);
    }

    public List<Picture> PagingBySearchedTitle(String title, int page) {
        return pictureRepository.PagingBySearchedTitle(title, page);
    }

    public int SearchedTitleSize(String title) {
        return pictureRepository.findBySearchedTitle(title).size();
    }

    //팔로우한 멤버 사진 조회(랜덤)
    public List<Picture> recommendPictureByFollow(Long memberId) {
        Member member = memberRepository.findOne(memberId);
        List<Follow> follows = followRepository.findByFollowMember(member);

        List<Picture> pictures = new ArrayList<>();
        for (Follow follow : follows) {
            List<Picture> followPictures = pictureRepository.findByMember(follow.getFollowedMember());
            pictures.addAll(followPictures);
        }
        Collections.shuffle(pictures);
        return pictures;
    }

    public List<Picture> findByAllOrderedLikes() {
        List<Picture> pictures = pictureRepository.findByAllOrderedLikes();
        return pictures;
    }

    public List<FollowPictureDTO> getFollowAndPictures(Long memberId) {
        Member member = memberRepository.findOne(memberId);
        List<Follow> followsByFollowingMember = followRepository.findByFollowMember(member);
        List<FollowPictureDTO> followPictureDTOList = new ArrayList<>();

        for (Follow follow : followsByFollowingMember) {
            FollowPictureDTO followPictureDTO = new FollowPictureDTO();
            Member followedMember = follow.getFollowedMember();
            followPictureDTO.setFollowedMember(followedMember);

            boolean followCheck = followService.followCheck(member.getId(), followedMember.getId());
            followPictureDTO.setFollowCheck(followCheck);

            List<Picture> pictures = pictureRepository.findByMemberOrderedLikes(followedMember);
            followPictureDTO.setFollowedPictures(pictures);
            followPictureDTOList.add(followPictureDTO);
        }
        return followPictureDTOList;
    }

    public List<FollowPictureDTO> getOtherFollowAndPictures(Long memberId, Long loginId) {
        Member member = memberRepository.findOne(memberId);
        Member loginMember = memberRepository.findOne(loginId);
        List<Follow> followsByFollowingMember = followRepository.findByFollowMember(member);
        List<FollowPictureDTO> followPictureDTOList = new ArrayList<>();

        for (Follow follow : followsByFollowingMember) {
            FollowPictureDTO followPictureDTO = new FollowPictureDTO();
            Member followedMember = follow.getFollowedMember();
            followPictureDTO.setFollowedMember(followedMember);

            boolean followCheck = followService.followCheck(loginMember.getId(), followedMember.getId());
            followPictureDTO.setFollowCheck(followCheck);

            List<Picture> pictures = pictureRepository.findByMemberOrderedLikes(followedMember);
            followPictureDTO.setFollowedPictures(pictures);
            followPictureDTOList.add(followPictureDTO);
        }
        return followPictureDTOList;
    }
}
