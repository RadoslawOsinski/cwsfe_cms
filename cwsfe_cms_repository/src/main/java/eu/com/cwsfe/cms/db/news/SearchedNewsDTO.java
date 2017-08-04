package eu.com.cwsfe.cms.db.news;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.time.ZonedDateTime;

/**
 * Created by Radosław Osiński
 */
public class SearchedNewsDTO {

    private Long id;
    private String author;
    private Long newsTypeId;
    private Long folderId;
    private ZonedDateTime creationDate;
    private String newsCode;
    private String status;

    public SearchedNewsDTO(Long id, String author, Long newsTypeId, Long folderId, ZonedDateTime creationDate, String newsCode, String status) {
        this.id = id;
        this.author = author;
        this.newsTypeId = newsTypeId;
        this.folderId = folderId;
        this.creationDate = creationDate;
        this.newsCode = newsCode;
        this.status = status;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getNewsCode() {
        return newsCode;
    }

    public void setNewsCode(String newsCode) {
        this.newsCode = newsCode;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getFolderId() {
        return folderId;
    }

    public void setFolderId(Long folderId) {
        this.folderId = folderId;
    }

    public Long getNewsTypeId() {
        return newsTypeId;
    }

    public void setNewsTypeId(Long newsTypeId) {
        this.newsTypeId = newsTypeId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("author", author)
            .append("newsCode", newsCode)
            .append("creationDate", creationDate)
            .append("id", id)
            .append("status", status)
            .append("folderId", folderId)
            .append("newsTypeId", newsTypeId)
            .toString();
    }
}
