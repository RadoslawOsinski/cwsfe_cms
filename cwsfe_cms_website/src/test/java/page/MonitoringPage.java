package page;

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
        assertEquals("http://localhost:8080/CWSFE_CMS/monitoring/generalInformation", driver.getCurrentUrl());
        return driver.findElement(By.tagName("table")).isDisplayed();
    }
}
