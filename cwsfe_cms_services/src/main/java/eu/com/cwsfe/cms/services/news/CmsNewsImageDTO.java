package eu.com.cwsfe.cms.services.news;

import eu.com.cwsfe.cms.db.common.CmsImage;
import eu.com.cwsfe.cms.db.news.CmsNewsImageStatus;

public class CmsNewsImageDTO extends CmsImage {

    private Long newsId;
    private CmsNewsImageStatus status;

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public CmsNewsImageStatus getStatus() {
        return status;
    }

    public void setStatus(CmsNewsImageStatus status) {
        this.status = status;
    }

}
