package eu.com.cwsfe.cms.db.news;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class CmsTextI18nRepository {

    public Long countForAjax(Session session) {
        Query query = session.getNamedQuery(CmsTextI18NEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (Long) query.getSingleResult();
    }

    public List<CmsTextI18NEntity> list(Session session) {
        Query query = session.getNamedQuery(CmsTextI18NEntity.LIST);
        return query.list();
    }

    public List<CmsTextI18NEntity> listAjax(Session session, int offset, int limit) {
        Query query = session.getNamedQuery(CmsTextI18NEntity.LIST);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    public Optional<CmsTextI18NEntity> get(Session session, Long id) {
        return Optional.ofNullable(session.get(CmsTextI18NEntity.class, id));
    }

    public Optional<String> findTranslation(Session session, String language2LetterCode, String category, String key) {
        Query query = session.getNamedQuery(CmsTextI18NEntity.FIND_TRANSLATION);
        query.setParameter("language2LetterCode", language2LetterCode);
        query.setParameter("category", category);
        query.setParameter("key", key);
        return query.uniqueResultOptional();
    }

    public Long add(Session session, CmsTextI18NEntity cmsTextI18n) {
        session.saveOrUpdate(cmsTextI18n);
        session.flush();
        return cmsTextI18n.getId();
    }

    public void update(Session session, CmsTextI18NEntity cmsTextI18n) {
        session.update(cmsTextI18n);
    }

    public void delete(Session session, CmsTextI18NEntity cmsTextI18n) {
        session.delete(cmsTextI18n);
    }

    public Optional<CmsTextI18NEntity> get(Session session, long langId, long i18nCategory, String i18NKey) {
            Query query = session.getNamedQuery(CmsTextI18NEntity.GET_EXISTING);
            query.setParameter("langId", langId);
            query.setParameter("i18nCategory", i18nCategory);
            query.setParameter("i18NKey", i18NKey);
            return (Optional<CmsTextI18NEntity>) query.uniqueResultOptional();
    }
}
