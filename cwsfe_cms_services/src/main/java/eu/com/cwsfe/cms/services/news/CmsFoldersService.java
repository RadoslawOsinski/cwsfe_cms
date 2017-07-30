package eu.com.cwsfe.cms.services.news;

import eu.com.cwsfe.cms.db.news.CmsFoldersEntity;
import eu.com.cwsfe.cms.db.news.CmsFoldersRepository;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

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


    public List<CmsFoldersEntity> listAjax(int iDisplayStart, int iDisplayLength) {
        return cmsFoldersRepository.listAjax(sessionFactory.getCurrentSession(), iDisplayStart, iDisplayLength);
    }

    public int countForAjax() {
        return cmsFoldersRepository.countForAjax(sessionFactory.getCurrentSession());
    }

    public List<CmsFoldersEntity> listFoldersForDropList(String term, Integer limit) {
        return cmsFoldersRepository.listFoldersForDropList(sessionFactory.getCurrentSession(), term, limit);
    }

    public void add(CmsFoldersEntity cmsFolder) {
        cmsFoldersRepository.add(sessionFactory.getCurrentSession(), cmsFolder);
    }

    public void delete(CmsFoldersEntity cmsFolder) {
        cmsFoldersRepository.delete(sessionFactory.getCurrentSession(), cmsFolder);
    }
}
