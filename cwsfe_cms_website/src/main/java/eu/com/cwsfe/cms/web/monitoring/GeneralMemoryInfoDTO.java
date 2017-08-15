package eu.com.cwsfe.cms.web.monitoring;

/**
 * Created by Radosław Osiński
 */
public class GeneralMemoryInfoDTO {

    private String usedMemoryInMb;
    private String availableMemoryInMB;

    public void setUsedMemoryInMb(String usedMemoryInMb) {
        this.usedMemoryInMb = usedMemoryInMb;
    }

    public String getUsedMemoryInMb() {
        return usedMemoryInMb;
    }

    public void setAvailableMemoryInMB(String availableMemoryInMB) {
        this.availableMemoryInMB = availableMemoryInMB;
    }

    public String getAvailableMemoryInMB() {
        return availableMemoryInMB;
    }
}
