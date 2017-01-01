package integration.page.users;

import integration.page.layout.MenuPage;
import integration.page.login.LoginPage;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.junit.Assert.assertTrue;

/**
 * Created by Radosław Osiński
 */
@Ignore("Automate this test with profile usage on jenkins+gradle+sonarqube")
public class UsersListPageTest {

    private static WebDriver driver;

    private static MenuPage menuPage;

    @Test
    public void shouldOpenUserListPage() {
        //when
        UsersListPage userListPage = menuPage.goToUsersListPage();

        //then
        assertTrue(userListPage.isOpened());
    }

    @Test
    public void shouldAddUser() {
        //given
        UsersListPage userListPage = menuPage.goToUsersListPage();

        //when
        userListPage = userListPage.addUser();

        //then
        assertTrue(userListPage.isUserAdded());
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
