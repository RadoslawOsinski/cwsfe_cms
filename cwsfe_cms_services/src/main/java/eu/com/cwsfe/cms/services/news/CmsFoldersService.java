package eu.com.cwsfe.cms.services.news;

import eu.com.cwsfe.cms.db.news.CmsFoldersEntity;
import eu.com.cwsfe.cms.db.news.CmsFoldersRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Radosław Osiński
 */
@Service
public class CmsFoldersService {

    private final CmsFoldersRepository cmsFoldersRepository;
    private final SessionFactory sessionFactory;

    public CmsFoldersService(CmsFoldersRepository cmsFoldersRepository, SessionFactory sessionFactory) {
        this.cmsFoldersRepository = cmsFoldersRepository;
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public List<CmsFoldersEntity> listAjax(int iDisplayStart, int iDisplayLength) {
        return cmsFoldersRepository.listAjax(sessionFactory.getCurrentSession(), iDisplayStart, iDisplayLength);
    }

    @Transactional
    public int countForAjax() {
        return cmsFoldersRepository.countForAjax(sessionFactory.getCurrentSession());
    }

    @Transactional
    public List<CmsFoldersEntity> listFoldersForDropList(String term, Integer limit) {
        return cmsFoldersRepository.listFoldersForDropList(sessionFactory.getCurrentSession(), term, limit);
    }

    @Transactional
    public void add(CmsFoldersEntity cmsFolder) {
        cmsFoldersRepository.add(sessionFactory.getCurrentSession(), cmsFolder);
    }

    @Transactional
    public void delete(CmsFoldersEntity cmsFolder) {
        cmsFoldersRepository.delete(sessionFactory.getCurrentSession(), cmsFolder);
    }
}
