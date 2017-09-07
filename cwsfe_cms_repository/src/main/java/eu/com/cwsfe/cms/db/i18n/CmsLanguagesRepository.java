package eu.com.cwsfe.cms.db.i18n;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CmsLanguagesRepository {

    public Long countForAjax(Session session) {
        Query query = session.getNamedQuery(CmsLanguagesEntity.COUNT_FOR_AJAX_N);
        return (Long) query.uniqueResult();
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

    public Optional<CmsLanguagesEntity> getById(Session session, Long id) {
        return Optional.ofNullable(session.get(CmsLanguagesEntity.class, id));
    }

    public Optional<CmsLanguagesEntity> getByCode(Session session, String code) {
        Query query = session.getNamedQuery(CmsLanguagesEntity.GET_BY_CODE);
        query.setParameter("code", code);
        return (Optional<CmsLanguagesEntity>) query.uniqueResultOptional();
    }

    public Optional<CmsLanguagesEntity> getByCodeIgnoreCase(Session session, String code) {
        Query query = session.getNamedQuery(CmsLanguagesEntity.GET_BY_CODE_IGNORE_CASE);
        query.setParameter("code", code);
        return (Optional<CmsLanguagesEntity>) query.uniqueResultOptional();
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
        CmsLanguagesEntity cmsLanguagesEntity = session.get(CmsLanguagesEntity.class, language.getId());
        cmsLanguagesEntity.setStatus("DELETED");
        session.update(cmsLanguagesEntity);
    }

    public void undelete(Session session, CmsLanguagesEntity language) {
        CmsLanguagesEntity cmsLanguagesEntity = session.get(CmsLanguagesEntity.class, language.getId());
        cmsLanguagesEntity.setStatus("NEW");
        session.update(cmsLanguagesEntity);
    }

}
