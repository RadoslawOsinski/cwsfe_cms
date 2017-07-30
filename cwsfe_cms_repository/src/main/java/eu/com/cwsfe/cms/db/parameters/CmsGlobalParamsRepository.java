package eu.com.cwsfe.cms.db.parameters;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CmsGlobalParamsRepository {

    public int countForAjax(Session session) {
        Query query = session.getNamedQuery(CmsGlobalParamsEntity.TOTAL_NUMBER_NOT_DELETED_QUERY);
        return (int) query.getSingleResult();
    }

    public List<CmsGlobalParamsEntity> list(Session session) {
        Query query = session.getNamedQuery(CmsGlobalParamsEntity.LIST);
        return query.list();
    }

    public List<CmsGlobalParamsEntity> listAjax(Session session, int offset, int limit) {
        Query query = session.getNamedQuery(CmsGlobalParamsEntity.LIST);
        query.setMaxResults(limit);
        query.setFirstResult(offset);
        return query.getResultList();
    }

    public List<CmsGlobalParamsEntity> listForDropList(Session session, String term, int limit) {
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<CmsGlobalParamsEntity> query = criteriaBuilder.createQuery(CmsGlobalParamsEntity.class);
        Root<CmsGlobalParamsEntity> params = query.from(CmsGlobalParamsEntity.class);
        query.select(params);
        query.where(
            criteriaBuilder.like(criteriaBuilder.lower(params.get("code")), "%" + term.toLowerCase() + "%")
        );
        List<Order> orders = new ArrayList<>();
        orders.add(criteriaBuilder.asc(params.get("code")));
        query.orderBy(orders);
        return session.createQuery(query)
            .setMaxResults(limit)
            .list();
    }

    public CmsGlobalParamsEntity get(Session session, Long id) {
        return session.get(CmsGlobalParamsEntity.class, id);
    }

    //todo move transactional to service!
    @Transactional
    public CmsGlobalParamsEntity getByCode(Session session, String code) {
        Query query = session.getNamedQuery(CmsGlobalParamsEntity.GET_BY_CODE);
        query.setParameter("code", code);
        return (CmsGlobalParamsEntity) query.getSingleResult();
    }

    public Long add(Session session, CmsGlobalParamsEntity cmsGlobalParam) {
        session.saveOrUpdate(cmsGlobalParam);
        session.flush();
        return cmsGlobalParam.getId();
    }

    public void update(Session session, CmsGlobalParamsEntity cmsGlobalParam) {
        session.update(cmsGlobalParam);
    }

    public void delete(Session session, CmsGlobalParamsEntity cmsGlobalParam) {
        session.delete(cmsGlobalParam);
    }

}
