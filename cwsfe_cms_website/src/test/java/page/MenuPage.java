package page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Created by Radosław Osiński
 */
public class MenuPage extends Page {

    public MenuPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOpened() {
        return driver.findElement(By.className("side-nav")).isDisplayed();
    }

    public MonitoringPage goToMonitoringPage() {
        driver.findElement(By.partialLinkText("Monitoring")).click();
        return new MonitoringPage(driver);
    }

}
