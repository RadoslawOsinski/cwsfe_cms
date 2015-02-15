package integration.page.users;

import integration.page.Page;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Radosław Osiński
 */
public class UsersListPage extends Page {

    public static final String EXISTING_USER = "CWSFE_CMS_ADMIN";

    public UsersListPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOpened() {
        assertEquals(getCmsBaseUrl() + "/users", driver.getCurrentUrl());
        return driver.findElement(By.id("usersList")).isDisplayed();
    }

    public UsersListPage addUser() {
        driver.findElement(By.id("userName")).sendKeys(EXISTING_USER);
        driver.findElement(By.id("passwordHash")).sendKeys(EXISTING_USER);
        driver.findElement(By.id("addUserButton")).click();
        return new UsersListPage(driver);
    }

    public boolean isUserAdded() {
        driver.findElement(By.id("addUserButton")).click();

        WebElement baseTable = driver.findElement(By.id("usersList"));
        List<WebElement> tableRows = baseTable.findElements(By.tagName("tr"));
        boolean userAdded = false;
        for (WebElement tableRow : tableRows) {
            if (tableRow.getText().contains(EXISTING_USER)) {
                userAdded = true;
            }
        }
        return userAdded;
    }

}
