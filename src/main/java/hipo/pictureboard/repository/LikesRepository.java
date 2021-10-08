package hipo.pictureboard.repository;

import hipo.pictureboard.domain.Likes;
import hipo.pictureboard.domain.Member;
import hipo.pictureboard.domain.Picture;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikesRepository {

    private final EntityManager em;

    public void save(Likes likes) {
        em.persist(likes);
    }

    public List<Likes> findByMemberAndPicture(Member member, Picture picture) {
        return em.createQuery("select l from Likes l " +
                        "where l.member = :member " +
                        "and l.picture = :picture", Likes.class)
                .setParameter("member", member)
                .setParameter("picture", picture)
                .getResultList();
    }

}
