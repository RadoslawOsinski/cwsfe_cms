package eu.com.cwsfe.cms.services.news;

public class CmsNewsI18nPairDTO {

    private CmsNewsDTO cmsNewsDTO;

    private CmsNewsI18nContentDTO cmsNewsI18nContentDTO;

    CmsNewsI18nPairDTO(CmsNewsDTO cmsNewsDTO, CmsNewsI18nContentDTO cmsNewsI18nContentDTO) {
        this.cmsNewsDTO = cmsNewsDTO;
        this.cmsNewsI18nContentDTO = cmsNewsI18nContentDTO;
    }

    public CmsNewsDTO getCmsNewsDTO() {
        return cmsNewsDTO;
    }

    public void setCmsNewsDTO(CmsNewsDTO cmsNewsDTO) {
        this.cmsNewsDTO = cmsNewsDTO;
    }

    public CmsNewsI18nContentDTO getCmsNewsI18nContentDTO() {
        return cmsNewsI18nContentDTO;
    }

    public void setCmsNewsI18nContentDTO(CmsNewsI18nContentDTO cmsNewsI18nContentDTO) {
        this.cmsNewsI18nContentDTO = cmsNewsI18nContentDTO;
    }

}
