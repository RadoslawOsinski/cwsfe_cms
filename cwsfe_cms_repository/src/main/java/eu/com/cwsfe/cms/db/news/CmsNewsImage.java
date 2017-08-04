package eu.com.cwsfe.cms.db.news;

import eu.com.cwsfe.cms.db.common.CmsImage;
import eu.com.cwsfe.cms.db.common.NewDeletedStatus;

public class CmsNewsImage extends CmsImage {

    private static final long serialVersionUID = -1585676901660430169L;

    private Long newsId;
    private NewDeletedStatus status;

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public NewDeletedStatus getStatus() {
        return status;
    }

    public void setStatus(NewDeletedStatus status) {
        this.status = status;
    }

}
