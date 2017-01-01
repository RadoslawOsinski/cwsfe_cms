package integration.page.layout;

import integration.page.roles.RolesPage;
import integration.page.users.UsersListPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import integration.page.monitoring.MonitoringPage;
import integration.page.Page;

/**
 * Created by Radosław Osiński
 */
public class MenuPage extends Page {

    public MenuPage(WebDriver driver) {
        super(driver);
    }

    public boolean isOpened() {
        return driver.findElement(By.className("side-nav")).isDisplayed();
    }

    public MonitoringPage goToMonitoringPage() {
        driver.findElement(By.id("menuMonitoringLink")).click();
        return new MonitoringPage(driver);
    }

    public UsersListPage goToUsersListPage() {
        driver.findElement(By.id("menuUsersSubMenu")).click();
        driver.findElement(By.id("menuUsersLink")).click();
        return new UsersListPage(driver);
    }

    public RolesPage goToRolesPage() {
        driver.findElement(By.id("menuRolesLink")).click();
        return new RolesPage(driver);
    }
}
