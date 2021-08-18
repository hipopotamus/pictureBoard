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

    public List<Likes> findByPicture(Picture picture) {
        return em.createQuery("select l from Likes l join l.picture p" +
                        " where p.id = :pictureId", Likes.class)
                .setParameter("pictureId", picture.getId())
                .getResultList();
    }

    public List<Likes> findByOneMember(Member member, Picture picture) {
        return em.createQuery("select l from Likes l join l.picture p" +
                        " where p.id = :pictureId" +
                        " and l.member.id = :memberId", Likes.class)
                .setParameter("pictureId", picture.getId())
                .setParameter("memberId", member.getId())
                .getResultList();
    }
}
