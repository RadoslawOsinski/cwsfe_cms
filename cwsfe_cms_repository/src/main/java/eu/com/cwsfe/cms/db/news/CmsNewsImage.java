package eu.com.cwsfe.cms.db.news;

import eu.com.cwsfe.cms.db.common.CmsImage;
import eu.com.cwsfe.cms.db.common.NewDeletedStatus;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class CmsNewsImage extends CmsImage {

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

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.NO_CLASS_NAME_STYLE)
            .appendSuper(super.toString())
            .append("newsId", newsId)
            .append("status", status)
            .toString();
    }
}
