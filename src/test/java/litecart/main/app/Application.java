package litecart.main.app;

import litecart.main.pages.CartPage;
import litecart.main.pages.MainPage;
import litecart.main.pages.ProductPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Application {

    private final MainPage mainPage;
    private final ProductPage productPage;
    private final CartPage cartPage;

    public Application(WebDriver wd, WebDriverWait wait) {
        mainPage = new MainPage(wd);
        productPage = new ProductPage(wd, wait);
        cartPage = new CartPage(wd, wait);
    }


    public MainPage mainPage() {
        return mainPage;
    }

    public ProductPage productPage() {
        return productPage;
    }

    public CartPage cartPage() {
        return cartPage;
    }
}
