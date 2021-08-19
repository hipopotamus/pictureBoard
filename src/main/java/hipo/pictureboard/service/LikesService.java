package hipo.pictureboard.service;

import hipo.pictureboard.domain.*;
import hipo.pictureboard.repository.LikesRepository;
import hipo.pictureboard.repository.MemberRepository;
import hipo.pictureboard.repository.PictureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LikesService {

    private final MemberRepository memberRepository;
    private final PictureRepository pictureRepository;
    private final LikesRepository likesRepository;

    @Transactional
    public Likes create(Long memberId, Long pictureId) {
        Member member = memberRepository.findOne(memberId);
        Picture picture = pictureRepository.findOne(pictureId);
        Likes likes = Likes.createLikes(member, picture);
        likesRepository.save(likes);
        return likes;
    }

    public boolean findByOneMember(Long memberId, Long pictureId) {
        Member member = memberRepository.findOne(memberId);
        Picture picture = pictureRepository.findOne(pictureId);
        List<Likes> likesList = likesRepository.findByOneMember(member, picture);

        if (likesList.isEmpty() || likesList.get(0).getStatus() == OneClickStatus.CANCEL) {
            return false;
        } else {
            return true;
        }
    }

    @Transactional
    public Likes oneClick(Long memberId, Long pictureId) {
        Member member = memberRepository.findOne(memberId);
        Picture picture = pictureRepository.findOne(pictureId);
        List<Likes> likesList = likesRepository.findByOneMember(member, picture);

        log.info("like상태는? = {}", likesList.isEmpty());
        if (likesList.isEmpty()) {
            Likes createdLikes = create(memberId, pictureId);
            picture.addLikeCount();
            return createdLikes;
        } else {
            Likes getLikes = likesList.get(0);
            if (getLikes.getStatus() == OneClickStatus.CLICK) {
                getLikes.setStatus(OneClickStatus.CANCEL);
                picture.removeLikeCount();
                return getLikes;
            } else {
                getLikes.setStatus(OneClickStatus.CLICK);
                picture.addLikeCount();
                return getLikes;
            }
        }
    }

}
