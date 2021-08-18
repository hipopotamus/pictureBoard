package hipo.pictureboard.repository;

import hipo.pictureboard.domain.Follow;
import hipo.pictureboard.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FollowRepository {

    private final EntityManager em;

    public void save(Follow follow) {
        em.persist(follow);
    }

    public List<Follow> findByFollowMember(Member followMember) {
        return em.createQuery("select f from Follow f join f.followMember m" +
                    " where m.id = :followMemberId", Follow.class)
                    .setParameter("followMemberId", followMember.getId())
                    .getResultList();
    }

    public List<Follow> findByFollowerMember(Member followedMember) {
        return em.createQuery("select f from Follow f" +
                        " where f.followedMember.id = :followedMemberId", Follow.class)
                .setParameter("followedMemberId", followedMember.getId())
                .getResultList();
    }

    public List<Follow> findByFollowOneMember(Member followMember, Member followedMember) {
        return em.createQuery("select f from Follow f join f.followMember m" +
                        " where f.followedMember.id = :followedMember" +
                        " and m.id = :followMemberId", Follow.class)
                .setParameter("followedMember", followedMember.getId())
                .setParameter("followMemberId", followMember.getId())
                .getResultList();
    }
}
