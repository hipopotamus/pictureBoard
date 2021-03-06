package hipo.pictureboard.repository;

import hipo.pictureboard.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PictureRepository{

    private final EntityManager em;

    public void save(Picture picture) {
        em.persist(picture);
    }

    public Picture findOne(Long id) {
        return em.find(Picture.class, id);
    }


    //전체 조회 관련
    public List<Picture> PagingByAll(int page) {
        if (page < 1) {
            page = 1;
        }
        return em.createQuery("select p from Picture p " +
                        "order by p.saveDate DESC", Picture.class)
                .setFirstResult((page - 1) * Page.maxPageResult)
                .setMaxResults(Page.maxPageResult)
                .getResultList();
    }

    public List<Picture> findByAll() {
        return em.createQuery("select p from Picture p " +
                        "order by p.saveDate DESC", Picture.class)
                .getResultList();
    }

    public List<Picture> findByAllOrderedLikes() {
        return em.createQuery("select p from Picture p " +
                "order by p.likeCount DESC", Picture.class)
                .getResultList();
    }

    public List<Picture> findByMember(Member member) {
        return em.createQuery("select p from Picture p " +
                        "where p.member = :member " +
                        "order by p.saveDate DESC", Picture.class)
                .setParameter("member", member)
                .getResultList();
    }


    public List<Picture> findByPictureType(PictureType pictureType) {
        return em.createQuery("select p from Picture p " +
                        "where p.pictureType = :pictureType " +
                        "order by p.saveDate DESC", Picture.class)
                .setParameter("pictureType", pictureType)
                .getResultList();
    }

    public List<Picture> PagingByPictureType(PictureType pictureType, int page) {
        if (page < 1) {
            page = 1;
        }
        return em.createQuery("select p from Picture p " +
                        "where p.pictureType = :pictureType " +
                        "order by p.saveDate DESC", Picture.class)
                .setParameter("pictureType", pictureType)
                .setFirstResult((page - 1) * Page.maxPageResult)
                .setMaxResults(Page.maxPageResult)
                .getResultList();
    }

    public List<Picture> findBySearchedTitle(String title) {
        return em.createQuery("select p from Picture p " +
                        "where p.title " +
                        "like : title " +
                        "order by p.saveDate DESC", Picture.class)
                .setParameter("title", "%" +title + "%")
                .getResultList();
    }

    public List<Picture> PagingBySearchedTitle(String title, int page) {
        if (page < 1) {
            page = 1;
        }
        return em.createQuery("select p from Picture p " +
                        "where p.title " +
                        "like : title " +
                        "order by p.saveDate DESC", Picture.class)
                .setParameter("title", "%" +title + "%")
                .setFirstResult((page - 1) * Page.maxPageResult)
                .setMaxResults(Page.maxPageResult)
                .getResultList();
    }

    public List<Picture> findByMemberOrderedLikes(Member member) {
        return em.createQuery("select p from Picture p " +
                        "where p.member = : member " +
                        "order by p.likeCount DESC ", Picture.class)
                .setParameter("member", member)
                .setMaxResults(FollowConst.followPictureNumber)
                .getResultList();
    }

}
