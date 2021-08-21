package hipo.pictureboard.service;

import hipo.pictureboard.domain.*;
import hipo.pictureboard.repository.FollowRepository;
import hipo.pictureboard.repository.MemberRepository;
import hipo.pictureboard.repository.PictureRepository;
import hipo.pictureboard.web.dto.FollowPictureDTO;
import javassist.bytecode.LineNumberAttribute;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
        Picture picture = Picture.createPicture(title, content, pictureType, member, pictureImg);
        pictureRepository.save(picture);
        return picture;
    }

    public Picture findOne(Long pictureId) {
        return pictureRepository.findOne(pictureId);
    }

    public List<Picture> findAll() {
        return pictureRepository.findByAll();
    }

    public List<Picture> AllByPage(int page) {
        return pictureRepository.AllByPage(page);
    }

    public int AllPictureSize() {
        return pictureRepository.findByAll().size();
    }

    public List<Picture> findByMember(Long memberId) {
        Member member = memberRepository.findOne(memberId);
        return pictureRepository.findByMember(member);
    }

    public List<Picture> MemberByPage(Long memberId, int page) {
        Member member = memberRepository.findOne(memberId);
        return pictureRepository.MemberByPage(member, page);
    }

    public int MemberPictureSize(Long memberId) {
        Member member = memberRepository.findOne(memberId);
        return pictureRepository.findByMember(member).size();
    }

    public List<Picture> PictureTypeByPage(PictureType pictureType, int page) {
        return pictureRepository.PictureTypeByPage(pictureType, page);
    }

    public int PictureTypePictureSize(PictureType pictureType) {
        return pictureRepository.findByPictureType(pictureType).size();
    }

    public List<Picture> searchByTitle(String title) {
        return pictureRepository.findByTitle(title);
    }

    public List<Picture> TitleByPage(String title, int page) {
        return pictureRepository.TitleByPage(title, page);
    }

    public int TitlePictureSize(String title) {
        return pictureRepository.findByTitle(title).size();
    }

    public List<Picture> recommendByFollow(Long memberId) {
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

    public List<Picture> rankByLikes() {
        List<Picture> pictures = pictureRepository.findByAll();
        pictures.sort(Comparator.comparing(Picture::getLikeCount, Comparator.reverseOrder()));
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

            boolean followCheck = followService.findByFollowOneMember(member.getId(), followedMember.getId());
            followPictureDTO.setFollowCheck(followCheck);

            List<Picture> pictures = pictureRepository.followPictureByLikes(followedMember);
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

            boolean followCheck = followService.findByFollowOneMember(loginMember.getId(), followedMember.getId());
            followPictureDTO.setFollowCheck(followCheck);

            List<Picture> pictures = pictureRepository.followPictureByLikes(followedMember);
            followPictureDTO.setFollowedPictures(pictures);
            followPictureDTOList.add(followPictureDTO);
        }
        return followPictureDTOList;
    }
}
