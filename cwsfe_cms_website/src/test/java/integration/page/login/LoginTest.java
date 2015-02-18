package integration.page.login;

import integration.page.layout.MainPage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.junit.Assert.assertTrue;

/**
 * Created by Radosław Osiński
 */
public class LoginTest {

    private static WebDriver driver;

    @Test
    public void shouldLogIntoApplication() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();

        MainPage mainPage = loginPage.correctLogin();

        assertTrue(mainPage.isOpened());
    }

    @Test
    public void shouldNotLogIntoApplication() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();

        LoginPage failedLoginPage = loginPage.incorrectLogin();

        assertTrue(failedLoginPage.isFailedLoginOpened());
    }

    @BeforeClass
    public static void setUp() {
        driver = new FirefoxDriver();
    }

    @AfterClass
    public static void tearDown() {
        driver.close();
    }
}