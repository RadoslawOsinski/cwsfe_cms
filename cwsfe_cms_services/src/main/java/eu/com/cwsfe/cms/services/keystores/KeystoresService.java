package eu.com.cwsfe.cms.services.keystores;

import eu.com.cwsfe.cms.db.keystores.KeystoresEntity;
import eu.com.cwsfe.cms.db.keystores.KeystoresRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

/**
 * Created by Radosław Osiński
 */
@Service
public class KeystoresService {

    private final KeystoresRepository keystoresRepository;
    private final SessionFactory sessionFactory;

    public KeystoresService(KeystoresRepository keystoresRepository, SessionFactory sessionFactory) {
        this.keystoresRepository = keystoresRepository;
        this.sessionFactory = sessionFactory;
    }

    public KeystoresEntity getByName(String frontendApplicationKeystore) {
        return keystoresRepository.getByName(sessionFactory.getCurrentSession(), frontendApplicationKeystore);
    }
}
