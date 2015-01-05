package eu.com.cwsfe.cms.model;

import java.io.Serializable;

public class CmsNewsI18nPair implements Serializable {

    private static final long serialVersionUID = -2521708732548778417L;

    public CmsNewsI18nPair(CmsNews cmsNews, CmsNewsI18nContent cmsNewsI18nContent) {
        this.cmsNews = cmsNews;
        this.cmsNewsI18nContent = cmsNewsI18nContent;
    }

    private CmsNews cmsNews;

    private CmsNewsI18nContent cmsNewsI18nContent;

    public CmsNews getCmsNews() {
        return cmsNews;
    }

    public void setCmsNews(CmsNews cmsNews) {
        this.cmsNews = cmsNews;
    }

    public CmsNewsI18nContent getCmsNewsI18nContent() {
        return cmsNewsI18nContent;
    }

    public void setCmsNewsI18nContent(CmsNewsI18nContent cmsNewsI18nContent) {
        this.cmsNewsI18nContent = cmsNewsI18nContent;
    }
}
