package hipo.pictureboard.service;

import hipo.pictureboard.domain.Member;
import hipo.pictureboard.domain.Picture;
import hipo.pictureboard.repository.MemberRepository;
import hipo.pictureboard.repository.PictureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;
    private final PictureRepository pictureRepository;

    public Member login(String loginId, String password) {
        List<Member> loginMembers = memberRepository.findByLoginId(loginId);
        if (loginMembers.isEmpty()) {
            return null;
        }

        Optional<Member> member = Optional.ofNullable(loginMembers.get(0));
        return member.filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }

    public boolean checkMyPicture(Long loginMemberId, Long pictureMemberId) {
        Member loginMember = memberRepository.findOne(loginMemberId);
        if (loginMember.getId() == pictureMemberId) {
            return false;
        }
        return true;
    }
}
