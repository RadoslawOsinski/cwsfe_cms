package integration.page.monitoring;

import integration.page.login.LoginPage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import integration.page.layout.MenuPage;

import static org.junit.Assert.assertTrue;

/**
 * Created by Radosław Osiński
 */
public class MonitoringPageTest {

    private static WebDriver driver;

    private static MenuPage menuPage;

    @Test
    public void shouldOpenMonitoringPage() {
        //when
        MonitoringPage monitoringPage = menuPage.goToMonitoringPage();

        //then
        assertTrue(monitoringPage.isOpened());
    }

    @BeforeClass
    public static void setUp() {
        driver = new FirefoxDriver();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.open();
        menuPage = loginPage.getMenuPage();
        assertTrue(menuPage.isOpened());
    }

    @AfterClass
    public static void tearDown() {
        driver.close();
    }
}
