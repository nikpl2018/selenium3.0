package litecart.admin;

import base.BaseTest;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

public class ClickAllMenu extends BaseTest {

    @Test
    public void clickOnMenu() {
        wd.get("http://localhost/litecart/admin/");
        login("admin", "admin");
        getMenuItemNames().stream()
                .peek(this::clickOnMenuItem)
                .peek(name -> checkH1Present())
                .flatMap(name -> getSubMenuItemNames().stream())
                .peek(this::clickOnSubMenuItem)
                .forEach(name -> checkH1Present());
        WebElement logout = wd.findElement(By.xpath("//a[@title='Logout']"));
        logout.click();
    }

    private List<String> getMenuItemNames() {
        return wd.findElements(By.cssSelector("ul#box-apps-menu > li")).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    private void clickOnMenuItem(String itemName) {
        wd.findElement(By.xpath(String.format("//ul[@id='box-apps-menu']/li[contains(., '%s')]", itemName))).click();
    }

    private List<String> getSubMenuItemNames() {
        return wd.findElements(By.cssSelector("ul.docs > li")).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    private void clickOnSubMenuItem(String itemName) {
        wd.findElement(By.xpath(String.format("//ul[@class='docs']/li[contains(., '%s')]", itemName))).click();
    }

    private void checkH1Present() {
        wd.findElement(By.tagName("h1"));
    }

}
