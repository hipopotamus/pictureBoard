package hipo.pictureboard.service;

import hipo.pictureboard.domain.*;
import hipo.pictureboard.repository.LikesRepository;
import hipo.pictureboard.repository.MemberRepository;
import hipo.pictureboard.repository.PictureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        Likes likes = new Likes(member, picture);
        likesRepository.save(likes);
        return likes;
    }

    public boolean likesCheck(Long memberId, Long pictureId) {
        Member member = memberRepository.findOne(memberId);
        Picture picture = pictureRepository.findOne(pictureId);
        List<Likes> likesList = likesRepository.findByMemberAndPicture(member, picture);

        if (likesList.isEmpty() || likesList.get(0).getStatus() == OnClickStatus.OFF) {
            return false;
        }
        return true;
    }

    @Transactional
    public void onClick(Long memberId, Long pictureId) {
        Member member = memberRepository.findOne(memberId);
        Picture picture = pictureRepository.findOne(pictureId);
        List<Likes> likesList = likesRepository.findByMemberAndPicture(member, picture);

        if (likesList.isEmpty()) {
            create(memberId, pictureId);
            picture.addLikeCount();
        } else {
            Likes getLikes = likesList.get(0);

            getLikes.switchStatus();
        }
    }

}
