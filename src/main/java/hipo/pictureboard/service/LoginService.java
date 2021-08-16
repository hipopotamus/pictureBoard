package hipo.pictureboard.service;

import hipo.pictureboard.domain.Member;
import hipo.pictureboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public Member login(String loginId, String password) {
        Optional<Member> member = Optional.ofNullable(memberRepository.findByLoginId(loginId).get(1));

        return member.filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }
}
