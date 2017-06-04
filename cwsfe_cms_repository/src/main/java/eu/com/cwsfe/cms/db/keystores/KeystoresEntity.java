package eu.com.cwsfe.cms.db.keystores;

import eu.com.cwsfe.cms.db.blog.CmsBlogPostCommentsEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Radoslaw Osinski.
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedQuery(name = KeystoresEntity.GET_BY_NAME, query = "SELECT k FROM KeystoresEntity k where name = :name")
@Table(name = "keystores")
public class KeystoresEntity {

    public static final String GET_BY_NAME = "KeystoresEntity.getByName";

    private long id;
    private String name;
    private byte[] content;

    @Id
    @Column(name = "id", nullable = false, precision = 0)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "keystores_s")
    @SequenceGenerator(name = "keystores_s", sequenceName = "keystores_s")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "content", nullable = false)
    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        KeystoresEntity that = (KeystoresEntity) o;

        return new EqualsBuilder()
            .append(id, that.id)
            .append(name, that.name)
            .append(content, that.content)
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
            .append(id)
            .append(name)
            .append(content)
            .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("id", id)
            .append("name", name)
            .append("content", content)
            .toString();
    }
}
