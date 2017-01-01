package integration.page.monitoring;

import integration.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertEquals;

/**
 * Created by Radosław Osiński
 */
public class MonitoringPage extends Page {

    public MonitoringPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOpened() {
        assertEquals(getCmsBaseUrl() + "/monitoring/generalInformation", driver.getCurrentUrl());
        return driver.findElement(By.tagName("table")).isDisplayed();
    }
}
