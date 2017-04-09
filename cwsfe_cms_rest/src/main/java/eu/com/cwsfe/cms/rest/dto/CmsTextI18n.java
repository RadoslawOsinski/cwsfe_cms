package eu.com.cwsfe.cms.model;

import java.io.Serializable;

/**
 * @author radek
 */
public class CmsTextI18n implements Serializable {

    private static final long serialVersionUID = 9073293230244227369L;

    private Long id;
    private Long langId;
    private Long i18nCategory;
    private String i18nKey;
    private String i18nText;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLangId() {
        return langId;
    }

    public void setLangId(Long langId) {
        this.langId = langId;
    }

    public Long getI18nCategory() {
        return i18nCategory;
    }

    public void setI18nCategory(Long i18nCategory) {
        this.i18nCategory = i18nCategory;
    }

    public String getI18nKey() {
        return i18nKey;
    }

    public void setI18nKey(String i18nKey) {
        this.i18nKey = i18nKey;
    }

    public String getI18nText() {
        return i18nText;
    }

    public void setI18nText(String i18nText) {
        this.i18nText = i18nText;
    }
}
