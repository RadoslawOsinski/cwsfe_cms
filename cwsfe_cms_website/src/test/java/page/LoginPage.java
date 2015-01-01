package page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

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
        driver.get("http://localhost:8080/CWSFE_CMS/");
        return this;
    }

    public MainPage correctLogin() {
        driver.findElement(By.name("userName")).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("admin");
        driver.findElement(By.xpath(LOGIN_BUTTON_XPATH)).click();
        return new MainPage(driver);
    }

    public LoginPage incorrectLogin() {
        driver.findElement(By.name("userName")).sendKeys("admin");
        driver.findElement(By.id("password")).sendKeys("adminXXXXX");
        driver.findElement(By.xpath(LOGIN_BUTTON_XPATH)).click();
        return new LoginPage(driver);
    }

    public boolean isFailedLoginOpened() {
        assertEquals("http://localhost:8080/CWSFE_CMS/loginFailed", driver.getCurrentUrl());
//        assertTrue(driver.findElement(By.cssSelector("some error message")).isDisplayed());
        return true;
    }
}
