package eu.com.cwsfe.cms.model;

import java.io.InputStream;

public class Keystore {

    private Integer id;
    private String name;
    private InputStream content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InputStream getContent() {
        return content;
    }

    public void setContent(InputStream content) {
        this.content = content;
    }
}
