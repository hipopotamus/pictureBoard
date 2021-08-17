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
        Member member = Member.createMember(loginId, password, nickName, profilePicture);
        join(member);
        return member;
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    public Long join(Member member) {
        validateDuplicateLoginId(member);
        validateDuplicateNickName(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateLoginId(Member member) {
        List<Member> findMembers = memberRepository.findByLoginId(member.getLoginId());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    private void validateDuplicateNickName(Member member) {
        List<Member> findMembers = memberRepository.findByNickName(member.getNickName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }
    }

}
