package hipo.pictureboard.service;

import hipo.pictureboard.domain.*;
import hipo.pictureboard.repository.FollowRepository;
import hipo.pictureboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;

    @Transactional
    public Follow create(Long followMemberId, Long followedMemberId) {
        Member followMember = memberRepository.findOne(followMemberId);
        Member followedMember = memberRepository.findOne(followedMemberId);
        Follow follow = new Follow(followMember, followedMember);
        followRepository.save(follow);
        return follow;
    }

    public List<Follow> findByFollowMember(Long followMemberId) {
        Member followMember = memberRepository.findOne(followMemberId);
        return followRepository.findByFollowMember(followMember);
    }

    public boolean followCheck(Long followMemberId, Long followedMemberId) {
        Member followMember = memberRepository.findOne(followMemberId);
        Member followedMember = memberRepository.findOne(followedMemberId);
        List<Follow> follows = followRepository.findByFollowAndFollowed(followMember, followedMember);

        if (follows.isEmpty() || follows.get(0).getStatus() == OnClickStatus.OFF) {
            return false;
        }
        return true;
    }

    @Transactional
    public void onClick(Long followMemberId, Long followedMemberId) {
        Member followMember = memberRepository.findOne(followMemberId);
        Member followedMember = memberRepository.findOne(followedMemberId);
        List<Follow> follows = followRepository.findByFollowAndFollowed(followMember, followedMember);

        if (follows.isEmpty()) {
            create(followMemberId, followedMemberId);
            followMember.addFollowing();
            followedMember.addFollower();
        } else {
            Follow getFollow = follows.get(0);

            getFollow.switchStatus();
        }
    }
}
