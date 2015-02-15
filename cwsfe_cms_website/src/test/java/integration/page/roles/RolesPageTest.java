package integration.page.roles;

import integration.page.layout.MenuPage;
import integration.page.login.LoginPage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.junit.Assert.assertTrue;

/**
 * Created by Radosław Osiński
 */
public class RolesPageTest {

    private static WebDriver driver;

    private static MenuPage menuPage;

    @Test
    public void shouldOpenRolesPage() {
        //when
        RolesPage rolesPage = menuPage.goToRolesPage();

        //then
        assertTrue(rolesPage.isOpened());
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
