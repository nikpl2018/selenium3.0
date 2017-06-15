package litecart.admin;

import base.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Set;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.equalTo;

public class CheckOpenLinksInNewWindow extends BaseTest {

    @Test
    public void checkOpenLinksInNewWindow() {
        wd.get("http://localhost/litecart/admin/");
        adminLogin("admin", "admin");
        clickOnMenuItem("Countries");
        goToAddNewCountry();
        List<WebElement> externalLinks = wd.findElements(By.cssSelector("form a[target='_blank']"));
        externalLinks.forEach(this::checkOpenLinkInNewWindow);
        WebElement logout = wd.findElement(By.xpath("//a[@title='Logout']"));
        logout.click();
    }

    private void clickOnMenuItem(String itemName) {
        wd.findElement(By.xpath(String.format("//ul[@id='box-apps-menu']/li[contains(., '%s')]", itemName))).click();
    }

    private void goToAddNewCountry() {
        WebElement addNewCountryButton = wd.findElement(By.xpath("//a[contains(., ' Add New Country')]"));
        addNewCountryButton.click();
    }

    private void checkOpenLinkInNewWindow(WebElement link) {
        String originalWindow = wd.getWindowHandle();
        Set<String> existingWindows = wd.getWindowHandles();
        link.click();
        String newWindow = wait.until(d -> {
            Set<String> currentWindows = d.getWindowHandles();
            if (currentWindows.size() > existingWindows.size()) {
                currentWindows.removeAll(existingWindows);
                return currentWindows.iterator().next();
            }
            return null;
        });
        wd.switchTo().window(newWindow);
        Assert.assertThat(wd.getCurrentUrl(), notNullValue());
        Assert.assertThat(wd.getCurrentUrl().isEmpty(), equalTo(false));
        wd.close();
        wd.switchTo().window(originalWindow);
    }

}
