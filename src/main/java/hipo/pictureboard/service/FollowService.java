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

    public Follow create(Long followMemberId, Long followedMemberId) {
        Member followMember = memberRepository.findOne(followMemberId);
        Member followedMember = memberRepository.findOne(followedMemberId);
        Follow follow = new Follow();
        follow = Follow.createFollow(followMember, followedMember);
        followRepository.save(follow);
        return follow;
    }

    public boolean findByFollowOneMember(Long followMemberId, Long followedMemberId) {
        Member followMember = memberRepository.findOne(followMemberId);
        Member followedMember = memberRepository.findOne(followedMemberId);
        List<Follow> follows = followRepository.findByFollowOneMember(followMember, followedMember);

        if (follows.isEmpty() || follows.get(1).getStatus() == OneClickStatus.CANCEL) {
            return false;
        } else {
            return true;
        }
    }

    public Follow oneClick(Long followMemberId, Long followedMemberId) {
        Member followMember = memberRepository.findOne(followMemberId);
        Member followedMember = memberRepository.findOne(followedMemberId);
        List<Follow> follows = followRepository.findByFollowOneMember(followMember, followedMember);

        if (follows.isEmpty()) {
            Follow createdFollow = create(followMemberId, followedMemberId);
            followMember.addFollowing();
            followedMember.addFollower();
            return createdFollow;
        } else {
            Follow getFollow = follows.get(1);
            if (getFollow.getStatus() == OneClickStatus.CLICK) {
                getFollow.setStatus(OneClickStatus.CANCEL);
                followMember.removeFollowing();
                followedMember.removeFollower();
                return getFollow;
            } else {
                getFollow.setStatus(OneClickStatus.CLICK);
                followMember.addFollowing();
                followedMember.addFollower();
                return getFollow;
            }
        }
    }
}
