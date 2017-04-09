//package eu.com.cwsfe.cms.db.entity;
//
//import eu.com.cwsfe.cms.db.common.NewDeletedStatus;
//import org.apache.commons.lang3.builder.EqualsBuilder;
//import org.apache.commons.lang3.builder.HashCodeBuilder;
//import org.apache.commons.lang3.builder.ToStringBuilder;
//import org.hibernate.annotations.*;
//import org.hibernate.annotations.Cache;
//import org.hibernate.annotations.NamedQuery;
//
//import javax.persistence.*;
//import javax.persistence.Entity;
//import javax.persistence.Table;
//
///**
// * Created by Radoslaw Osinski.
// */
//@Entity
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@Table(name = "blog_post_code")
//@NamedQuery(name = BlogPostCodeEntity.TOTAL_NUMBER_NOT_DELETED_QUERY, query = "SELECT count(bpc) FROM BlogPostCodeEntity bpc WHERE status <> 'D'")
//@NamedQuery(name = BlogPostCodeEntity.SEARCH_BY_AJAX_QUERY, query = "SELECT bpc FROM BlogPostCodeEntity bpc where status <> 'D' and blogPostId = :blogPostId order by codeId desc")
//@NamedQuery(name = BlogPostCodeEntity.SEARCH_BY_AJAX_COUNT_QUERY, query = "SELECT count(bpc) FROM BlogPostCodeEntity bpc where status <> 'D' and blogPostId = :blogPostId order by codeId desc")
//@NamedQuery(name = BlogPostCodeEntity.CODE_FOR_POST_BY_CODE_ID_QUERY, query = "SELECT count(bpc) FROM BlogPostCodeEntity bpc WHERE bpc.blogPostId = :blogPostId and codeId = :codeId")
//@NamedQuery(name = BlogPostCodeEntity.DELETE_QUERY, query = "DELETE FROM BlogPostCodeEntity bpc WHERE blogPostId = :blogPostId AND codeId = :codeId")
//public class BlogPostCodeEntity {
//
//    public static final String TOTAL_NUMBER_NOT_DELETED_QUERY = "BlogPostCodeEntity.getTotalNumberNotDeleted";
//    public static final String SEARCH_BY_AJAX_QUERY = "BlogPostCodeEntity.searchByAjax";
//    public static final String SEARCH_BY_AJAX_COUNT_QUERY = "BlogPostCodeEntity.searchByAjaxCount";
//    public static final String CODE_FOR_POST_BY_CODE_ID_QUERY = "BlogPostCodeEntity.getCodeForPostByCodeId";
//    public static final String DELETE_QUERY = "BlogPostCodeEntity.delete";
//
//    private String codeId;
//    private Long blogPostId;
//    private String code;
//
//    private NewDeletedStatus status;
//
//    @Id
//    @Column(name = "code_id", nullable = false, length = 100)
//    public String getCodeId() {
//        return codeId;
//    }
//
//    public void setCodeId(String codeId) {
//        this.codeId = codeId;
//    }
//
//    @Basic
//    @Column(name = "blog_post_id", nullable = false, precision = 0)
//    public Long getBlogPostId() {
//        return blogPostId;
//    }
//
//    public void setBlogPostId(Long blogPostId) {
//        this.blogPostId = blogPostId;
//    }
//
//    @Basic
//    @Column(name = "code", nullable = false, length = -1)
//    public String getCode() {
//        return code;
//    }
//
//    public void setCode(String code) {
//        this.code = code;
//    }
//
//    @Basic
//    @Column(name = "status", nullable = false, length = -1)
//    @Convert(converter = NewDeletedStatus.class)
//    public NewDeletedStatus getStatus() {
//        return status;
//    }
//
//    public void setStatus(NewDeletedStatus status) {
//        this.status = status;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//
//        if (o == null || getClass() != o.getClass()) return false;
//
//        BlogPostCodeEntity that = (BlogPostCodeEntity) o;
//
//        return new EqualsBuilder()
//            .append(blogPostId, that.blogPostId)
//            .append(codeId, that.codeId)
//            .append(code, that.code)
//            .append(status, that.status)
//            .isEquals();
//    }
//
//    @Override
//    public int hashCode() {
//        return new HashCodeBuilder(17, 37)
//            .append(codeId)
//            .append(blogPostId)
//            .append(code)
//            .append(status)
//            .toHashCode();
//    }
//
//    @Override
//    public String toString() {
//        return new ToStringBuilder(this)
//            .append("codeId", codeId)
//            .append("blogPostId", blogPostId)
//            .append("code", code)
//            .append("status", status)
//            .toString();
//    }
//}
