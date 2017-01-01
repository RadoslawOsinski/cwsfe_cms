package integration.page.roles;

import integration.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import static org.junit.Assert.assertEquals;

/**
 * Created by Radosław Osiński
 */
public class RolesPage extends Page {

    public RolesPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOpened() {
        assertEquals(getCmsBaseUrl() + "/roles", driver.getCurrentUrl());
        return driver.findElement(By.id("rolesList")).isDisplayed();
    }

}
