package integration.page;

import org.openqa.selenium.WebDriver;

/**
 * Created by Radosław Osiński
 */
public class Page {

    private static final String CWSFE_CMS_TEST_URL = "http://localhost:8080/CWSFE_CMS";

    protected final WebDriver driver;

    public Page(WebDriver driver) {
        this.driver = driver;
    }

    protected String getCmsBaseUrl() {
        return CWSFE_CMS_TEST_URL;
    }
}
