package litecart.main;

import base.BaseTest;
import litecart.main.app.Application;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class WorkingWithCartWithPageObjects extends BaseTest {

    @Test
    public void workingWithCart() {
        Application app = new Application(wd, wait);
        app.mainPage().open();
        app.mainPage().selectProduct(1);
        app.productPage().selectSizeIfAvailable(1);
        app.productPage().addToCart(1);
        app.productPage().goHome();
        app.mainPage().selectProduct(1);
        app.productPage().selectSizeIfAvailable(1);
        app.productPage().addToCart(1);
        app.productPage().goHome();
        app.mainPage().selectProduct(1);
        app.productPage().selectSizeIfAvailable(1);
        app.productPage().addToCart(1);
        app.productPage().goToCart();
        app.cartPage().deleteAllProducts();
    }
}
