package litecart.main.pages;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductPage {

    @FindBy(css = "span.quantity")
    private WebElement cartQuantityWe;

    @FindBy(css = "input[name = 'quantity']")
    private WebElement quantityField;

    @FindBy(css = "button[name = 'add_cart_product']")
    private WebElement addToCart;

    @FindBy(css = "select[name = 'options[Size]']")
    private WebElement sizeField;

    @FindBy(xpath = "//a[./img[@title='My Store']]")
    private WebElement home;

    @FindBy(xpath = "//a[contains(text(), 'Checkout')]")
    private WebElement checkout;

    private final WebDriverWait wait;

    public ProductPage(WebDriver wd, WebDriverWait wait) {
        PageFactory.initElements(wd, this);
        this.wait = wait;
    }

    public void addToCart(int quantity) {
        int beforeCartQuantity = Integer.parseInt(cartQuantityWe.getText());
        quantityField.clear();
        quantityField.sendKeys(String.valueOf(quantity));
        addToCart.click();
        wait.until(d -> Integer.parseInt(cartQuantityWe.getText()) >= beforeCartQuantity + quantity);
    }

    public void selectSizeIfAvailable(int sizeIndex) {
        try {
            Select productSize = new Select(sizeField);
            productSize.selectByIndex(sizeIndex);
        } catch (NoSuchElementException e) {}
    }

    public void goHome() {
        home.click();
    }

    public void goToCart() {
        checkout.click();
    }

}
