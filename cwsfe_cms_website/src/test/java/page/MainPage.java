package page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertEquals;

/**
 * Created by Radosław Osiński
 */
public class MainPage extends Page {

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOpened() {
        assertEquals("http://localhost:8080/CWSFE_CMS/Main", driver.getCurrentUrl());
        return driver.findElement(By.className("breadcrumbs")).isDisplayed();
    }
}
