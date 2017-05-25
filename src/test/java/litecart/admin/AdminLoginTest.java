package litecart.admin;

import base.BaseTest;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class AdminLoginTest extends BaseTest {

    @Test
    public void adminLogin() {
        wd.get("http://localhost/litecart/admin/");
        WebElement username = wd.findElement(By.xpath("//input[@name='username']"));
        username.sendKeys("admin");
        WebElement password = wd.findElement(By.xpath("//input[@name='password']"));
        password.sendKeys("admin");
        WebElement login = wd.findElement(By.xpath("//button[@name='login']"));
        login.click();
        WebElement logout = wd.findElement(By.xpath("//a[@title='Logout']"));
        logout.click();
    }

}
