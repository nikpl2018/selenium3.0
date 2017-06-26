package litecart.main.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class CartPage {

    @FindBy(css = "ul.items > li button[name = 'remove_cart_item']")
    private List<WebElement> deleteProductButtons;

    @FindBy(xpath = "//div[@id = 'order_confirmation-wrapper']/table//tr[.//td[@class='item']]")
    private List<WebElement> productTableRows;

    private final WebDriverWait wait;

    public CartPage(WebDriver wd, WebDriverWait wait) {
        PageFactory.initElements(wd, this);
        this.wait = wait;
    }

    public void deleteAllProducts() {
        while (deleteProductButtons.size() > 0) {
            WebElement deleteProduct = deleteProductButtons.get(0);
            int initialRowCount = deleteProductButtons.size();
            deleteProduct.click();
            wait.until(d -> initialRowCount > productTableRows.size());
        }
    }
}
