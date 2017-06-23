package litecart.admin;

import base.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.LogEntry;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;

public class CheckBrowserLogs extends BaseTest {

    @Test
    public void testBrowserLogs() {
        wd.get("http://localhost/litecart/admin/");
        adminLogin("admin", "admin");
        wd.get("http://localhost/litecart/admin/?app=catalog&doc=catalog&category_id=1");
        int productNameColumnIndex = getColumnIndexByName("Name");
        List<String> productsUrls = getProductsUrls(productNameColumnIndex);
        while (productsUrls.size() < 1) {
            List<WebElement> closedFolderLinks = getClosedCategoryLink(productNameColumnIndex);
            if (closedFolderLinks.size() < 1)
                throw new IllegalStateException("Закрытые категории продуктов не найдены");
            closedFolderLinks.get(0).click();
            productsUrls = getProductsUrls(productNameColumnIndex);
        }
        productsUrls.forEach(this::checkBrowserLogsForProduct);
        WebElement logout = wd.findElement(By.xpath("//a[@title='Logout']"));
        logout.click();
    }

    private int getColumnIndexByName(String name) {
        List<String> columnNames = wd.findElements(By.cssSelector("tr.header > th")).stream().map(WebElement::getText).collect(Collectors.toList());
        int columnIndex = columnNames.indexOf(name);
        if (columnIndex < 0)
            throw new IllegalArgumentException("Колонка с именем '" + name + "' не найдена");
        return columnIndex + 1;
    }

    private List<String> getProductsUrls(int productColumnIndex) {
        return wd.findElements(By.xpath(String.format("//table//tr[@class='row']//td[not(./i[contains(@class, 'fa-folder')]) and position() = %d]//a", productColumnIndex))).stream().map(el -> el.getAttribute("href")).collect(Collectors.toList());
    }

    private List<WebElement> getClosedCategoryLink(int categoryColumnIndex) {
        return wd.findElements(By.xpath(String.format("//table//tr[@class='row']//td[./i[contains(@class, 'fa-folder') and not(contains(@class, 'fa-folder-open'))]  and position() = %d]//a", categoryColumnIndex)));
    }

    private void checkBrowserLogsForProduct(String productUrl) {
        wd.get(productUrl);
        Assert.assertThat(wd.getCurrentUrl(), notNullValue());
        Assert.assertThat(wd.getCurrentUrl().isEmpty(), equalTo(false));
        List<LogEntry> logs = wd.manage().logs().get("browser").getAll();
        if (logs.size() > 0) {
            System.out.println("Сообщения в логе браузера:");
            logs.forEach(System.out::println);
        }
        Assert.assertThat(logs.size(), equalTo(0));
    }
}
