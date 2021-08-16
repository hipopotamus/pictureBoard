package hipo.pictureboard.service;

import hipo.pictureboard.domain.*;
import hipo.pictureboard.repository.FollowRepository;
import hipo.pictureboard.repository.MemberRepository;
import hipo.pictureboard.repository.PictureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PictureService {

    private final MemberRepository memberRepository;
    private final PictureRepository pictureRepository;
    private final FollowRepository followRepository;

    public List<Picture> findAll() {
        return pictureRepository.findByAll();
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
        pictures.sort(Comparator.comparing(Picture::getLikeCount));
        return pictures;
    }

    public List<Picture> searchByTitle(String title) {
        return pictureRepository.findByTitle(title);
    }

    public Picture create(String title, String content, PictureType pictureType,
                          Long memberId, Img pictureImg) {
        Picture picture = new Picture();
        Member member = memberRepository.findOne(memberId);
        picture.setTitle(title);
        picture.setContent(content);
        picture.setPictureType(pictureType);
        picture.setMember(member);
        picture.setPicture(pictureImg);
        pictureRepository.save(picture);
        return picture;
    }
}
