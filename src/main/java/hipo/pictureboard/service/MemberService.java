package hipo.pictureboard.service;

import hipo.pictureboard.domain.Img;
import hipo.pictureboard.domain.Member;
import hipo.pictureboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member create(String loginId, String password, String nickName, Img profilePicture) {
        validateDuplicateLoginId(loginId);
        validateDuplicateNickName(nickName);

        Member member = new Member(loginId, password, nickName, profilePicture);
        memberRepository.save(member);
        return member;
    }

    private void validateDuplicateLoginId(String loginId) {
        List<Member> findMembers = memberRepository.findByLoginId(loginId);
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    private void validateDuplicateNickName(String nickName) {
        List<Member> findMembers = memberRepository.findByNickName(nickName);
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }
    }

    public Member findByOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    public List<Member> findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId);
    }

    public List<Member> findByNickName(String nickName) {
        return memberRepository.findByNickName(nickName);
    }

}
