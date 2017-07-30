package eu.com.cwsfe.cms.db.i18n;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CmsLanguagesRepository {

    public int countForAjax(Session session) {
        Query query = session.getNamedQuery(CmsLanguagesEntity.COUNT_FOR_AJAX_N);
        return (int) query.uniqueResult();
    }

    public List<CmsLanguagesEntity> listAll(Session session) {
        Query query = session.getNamedQuery(CmsLanguagesEntity.LIST);
        return query.list();
    }

    public List<CmsLanguagesEntity> listAjax(Session session, int offset, int limit) {
        Query query = session.getNamedQuery(CmsLanguagesEntity.LIST);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    public List<CmsLanguagesEntity> listForDropList(Session session, String term, int limit) {
        Query query = session.getNamedQuery(CmsLanguagesEntity.LIST_FOR_DROP_LIST);
        query.setParameter("name", '%' + term + '%');
        query.setParameter("code", '%' + term + '%');
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public CmsLanguagesEntity getById(Session session, Long id) {
        return session.get(CmsLanguagesEntity.class, id);
    }

    public CmsLanguagesEntity getByCode(Session session, String code) {
        Query query = session.getNamedQuery(CmsLanguagesEntity.GET_BY_CODE);
        query.setParameter("code", code);
        return (CmsLanguagesEntity) query.getSingleResult();
    }

    public CmsLanguagesEntity getByCodeIgnoreCase(Session session, String code) {
        Query query = session.getNamedQuery(CmsLanguagesEntity.GET_BY_CODE_IGNORE_CASE);
        query.setParameter("code", code);
        return (CmsLanguagesEntity) query.getSingleResult();
    }

    public Long add(Session session, CmsLanguagesEntity language) {
        language.setStatus("NEW");
        session.saveOrUpdate(language);
        session.flush();
        return language.getId();
    }

    public void update(Session session, CmsLanguagesEntity language) {
        session.update(language);
    }

    public void delete(Session session, CmsLanguagesEntity language) {
        language.setStatus("DELETED");
        session.update(language);
    }

    public void undelete(Session session, CmsLanguagesEntity language) {
        language.setStatus("NEW");
        session.update(language);
    }

}
