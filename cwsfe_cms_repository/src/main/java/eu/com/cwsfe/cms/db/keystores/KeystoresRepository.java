package eu.com.cwsfe.cms.db.keystores;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class KeystoresRepository {

    private final SessionFactory sessionFactory;

    public KeystoresRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public long add(KeystoresEntity keystore) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.saveOrUpdate(keystore);
        currentSession.flush();
        return keystore.getId();
    }

    public void delete(KeystoresEntity keystore) {
        sessionFactory.getCurrentSession().delete(keystore);
    }

    public KeystoresEntity getByName(String name) {
        Query query = sessionFactory.getCurrentSession().getNamedQuery(KeystoresEntity.GET_BY_NAME);
        query.setParameter("name", name);
        return (KeystoresEntity) query.getSingleResult();
    }
}
