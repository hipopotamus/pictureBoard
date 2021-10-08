package hipo.pictureboard.repository;

import hipo.pictureboard.domain.Follow;
import hipo.pictureboard.domain.Member;
import hipo.pictureboard.domain.OnClickStatus;
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
        return em.createQuery("select f from Follow f " +
                        "where f.followMember = :followMember " +
                        "and f.status = :status", Follow.class)
                    .setParameter("followMember", followMember)
                    .setParameter("status", OnClickStatus.ON)
                    .getResultList();
    }

    public List<Follow> findByFollowAndFollowed(Member followMember, Member followedMember) {
        return em.createQuery("select f from Follow f " +
                        "where f.followMember = :followMember " +
                        "and f.followedMember = :followedMember", Follow.class)
                .setParameter("followMember", followMember)
                .setParameter("followedMember", followedMember)
                .getResultList();
    }

}
