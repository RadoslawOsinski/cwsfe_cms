package integration.page.login;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import integration.page.layout.MainPage;
import integration.page.layout.MenuPage;
import integration.page.Page;

import static org.junit.Assert.assertEquals;

/**
 * Created by Radosław Osiński
 */
public class LoginPage extends Page {

    public static final String LOGIN_BUTTON_XPATH = "//*[@id=\"emailLogin\"]/button";

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage open() {
        driver.get(getCmsBaseUrl());
        return this;
    }

    public MainPage correctLogin() {
        driver.findElement(By.name("userName")).sendKeys("CWSFE_CMS_ADMIN");
        driver.findElement(By.id("password")).sendKeys("admin");
        driver.findElement(By.xpath(LOGIN_BUTTON_XPATH)).click();
        return new MainPage(driver);
    }

    public MenuPage getMenuPage() {
        driver.findElement(By.name("userName")).sendKeys("CWSFE_CMS_ADMIN");
        driver.findElement(By.id("password")).sendKeys("admin");
        driver.findElement(By.xpath(LOGIN_BUTTON_XPATH)).click();
        return new MenuPage(driver);
    }

    public LoginPage incorrectLogin() {
        driver.findElement(By.name("userName")).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("adminXXXXX");
        driver.findElement(By.xpath(LOGIN_BUTTON_XPATH)).click();
        return new LoginPage(driver);
    }

    public boolean isFailedLoginOpened() {
        assertEquals(getCmsBaseUrl() + "/loginFailed", driver.getCurrentUrl());
        //todo add login error message
//        assertTrue(driver.findElement(By.cssSelector("some error message")).isDisplayed());
        return true;
    }
}
