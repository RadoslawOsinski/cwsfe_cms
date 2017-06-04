package eu.com.cwsfe.cms.db.i18n;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CmsLanguagesRepository {

    private final SessionFactory sessionFactory;

    public CmsLanguagesRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public int countForAjax() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsLanguagesEntity.COUNT_FOR_AJAX_N);
        return (int) query.uniqueResult();
    }

    public List<CmsLanguagesEntity> listAll() {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsLanguagesEntity.LIST);
        return query.list();
    }

    public List<CmsLanguagesEntity> listAjax(int offset, int limit) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsLanguagesEntity.LIST);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    public List<CmsLanguagesEntity> listForDropList(String term, int limit) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsLanguagesEntity.LIST_FOR_DROP_LIST);
        query.setParameter("name", '%' + term + '%');
        query.setParameter("code", '%' + term + '%');
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public CmsLanguagesEntity getById(Long id) {
        return sessionFactory.getCurrentSession().get(CmsLanguagesEntity.class, id);
    }

    public CmsLanguagesEntity getByCode(String code) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsLanguagesEntity.GET_BY_CODE);
        query.setParameter("code", code);
        return (CmsLanguagesEntity) query.getSingleResult();
    }

    public CmsLanguagesEntity getByCodeIgnoreCase(String code) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(CmsLanguagesEntity.GET_BY_CODE_IGNORE_CASE);
        query.setParameter("code", code);
        return (CmsLanguagesEntity) query.getSingleResult();
    }

    public Long add(CmsLanguagesEntity language) {
        language.setStatus("NEW");
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(language);
        currentSession.flush();
        return language.getId();
    }

    public void update(CmsLanguagesEntity language) {
        sessionFactory.getCurrentSession().update(language);
    }

    public void delete(CmsLanguagesEntity language) {
        language.setStatus("DELETED");
        sessionFactory.getCurrentSession().update(language);
    }

    public void undelete(CmsLanguagesEntity language) {
        language.setStatus("NEW");
        sessionFactory.getCurrentSession().update(language);
    }

}
