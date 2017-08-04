package integration.page.login;

import org.junit.Test;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.Assert.assertTrue;
/**
 * Created by Radosław Osiński
 */
//@Ignore("Automate this test with profile usage on jenkins+gradle+sonarqube")
public class LoginTest {

    public static final String LOGIN_BUTTON_XPATH = "//*[@id=\"emailLogin\"]/button";

    @Test
    public void shouldLogIntoApplication() {
        //given
        open("http://localhost:8081/CWSFE_CMS");
        $(By.name("userName")).sendKeys("CWSFE_CMS_ADMIN");
        $(By.id("password")).sendKeys("admin");

        //when
        $(By.xpath(LOGIN_BUTTON_XPATH)).click();

        //then
//        assertEquals("http://localhost:8080/CWSFE_CMS/Main");
        assertTrue($(By.className("breadcrumbs")).isDisplayed());
    }

    @Test
    public void shouldNotLogIntoApplication() {
        open("http://localhost:8081/CWSFE_CMS");
        $(By.name("userName")).sendKeys("admin");
        $(By.id("password")).sendKeys("adminXXXXX");

        //when
        $(By.xpath(LOGIN_BUTTON_XPATH)).click();

        //then
//        assertEquals("http://localhost:8080/CWSFE_CMS/loginFailed");
//        assertTrue(driver.findElement(By.cssSelector("some error message")).isDisplayed());
    }

}
