package integration.page.layout;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import integration.page.Page;

import static org.junit.Assert.assertEquals;

/**
 * Created by Radosław Osiński
 */
public class MainPage extends Page {

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOpened() {
        assertEquals(getCmsBaseUrl() + "/Main", driver.getCurrentUrl());
        return driver.findElement(By.className("breadcrumbs")).isDisplayed();
    }
}
