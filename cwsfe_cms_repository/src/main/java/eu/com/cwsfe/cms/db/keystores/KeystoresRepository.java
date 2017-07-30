package eu.com.cwsfe.cms.db.keystores;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class KeystoresRepository {

    public long add(Session session, KeystoresEntity keystore) {
        session.saveOrUpdate(keystore);
        session.flush();
        return keystore.getId();
    }

    public void delete(Session session, KeystoresEntity keystore) {
        session.delete(keystore);
    }

    public KeystoresEntity getByName(Session session, String name) {
        Query query = session.getNamedQuery(KeystoresEntity.GET_BY_NAME);
        query.setParameter("name", name);
        return (KeystoresEntity) query.getSingleResult();
    }
}
