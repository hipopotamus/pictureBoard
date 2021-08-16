package hipo.pictureboard.repository;

import hipo.pictureboard.domain.Member;
import hipo.pictureboard.domain.Picture;
import hipo.pictureboard.domain.PictureType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PictureRepository {

    private final EntityManager em;

    public void save(Picture picture) {
        em.persist(picture);
    }

    public Picture findOne(Long id) {
        return em.find(Picture.class, id);
    }

    public List<Picture> findByAll() {
        return em.createQuery("select p from Picture p", Picture.class)
                .getResultList();
    }

    public List<Picture> findByMember(Member member) {
        return em.createQuery("select p from Picture p join p.member m" +
                        " where m.id = :memberId", Picture.class)
                .setParameter("memberId", member.getId())
                .getResultList();
    }

    public List<Picture> findByPictureType(PictureType pictureType) {
        return em.createQuery("select p from Picture p" +
                " where p.pictureType = :pictureType", Picture.class)
                .setParameter("pictureType", pictureType)
                .getResultList();
    }

    public List<Picture> findByTitle(String title) {
        return em.createQuery("select p from Picture p" +
                " where p.title like : title", Picture.class)
                .setParameter("title", title)
                .getResultList();
    }

}
