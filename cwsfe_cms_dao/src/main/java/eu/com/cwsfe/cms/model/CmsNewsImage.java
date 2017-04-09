package eu.com.cwsfe.cms.model;

import eu.com.cwsfe.cms.db.domains.CmsNewsImageStatus;

public class CmsNewsImage extends CmsImage {

    private static final long serialVersionUID = -1585676901660430169L;

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
