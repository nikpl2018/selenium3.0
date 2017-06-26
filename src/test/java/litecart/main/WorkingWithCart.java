package litecart.main;

import base.BaseTest;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class WorkingWithCart extends BaseTest {

    @Test
    public void workingWithCart() {
        wd.get("http://localhost/litecart/");
        selectFirstProduct();
        addToCartCurrentProduct(1);
        goHome();
        selectFirstProduct();
        addToCartCurrentProduct(1);
        goHome();
        selectFirstProduct();
        addToCartCurrentProduct(1);
        goToCart();
        deleteAllProductsFromCart();
    }

    private void selectFirstProduct() {
        List<WebElement> products = wd.findElements(By.cssSelector("ul.products > li"));
        if (products.size() < 1)
            throw new IllegalStateException("В магазине нет доступных товаров");
        products.get(0).click();
    }

    private void addToCartCurrentProduct(int quantity) {
        WebElement cartQuantityWe = wd.findElement(By.cssSelector("span.quantity"));
        int beforeCartQuantity = Integer.parseInt(cartQuantityWe.getText());
        selectFirstSizeIfAvailable();
        WebElement quantityField = wd.findElement(By.cssSelector("input[name = 'quantity']"));
        quantityField.clear();
        quantityField.sendKeys(String.valueOf(quantity));
        WebElement addToCart = wd.findElement(By.cssSelector("button[name = 'add_cart_product']"));
        addToCart.click();
        wait.until(d -> Integer.parseInt(cartQuantityWe.getText()) >= beforeCartQuantity + quantity);
    }

    private void selectFirstSizeIfAvailable() {
        try {
            WebElement sizeField = wd.findElement(By.cssSelector("select[name = 'options[Size]']"));
            Select productSize = new Select(sizeField);
            productSize.selectByIndex(1);
        } catch (NoSuchElementException e) {}
    }

    private void goHome() {
        WebElement home = wd.findElement(By.xpath("//a[./img[@title='My Store']]"));
        home.click();
    }

    private void goToCart() {
        WebElement checkout = wd.findElement(By.xpath("//a[contains(text(), 'Checkout')]"));
        checkout.click();
    }

    private void deleteAllProductsFromCart() {
        List<WebElement> cartProducts = wd.findElements(By.cssSelector("ul.items > li"));
        while (cartProducts.size() > 0) {
            WebElement deleteProduct = cartProducts.get(0).findElement(By.cssSelector("button[name = 'remove_cart_item']"));
            WebElement cartTable = wd.findElement(By.cssSelector("div#order_confirmation-wrapper > table"));
            deleteProduct.click();
            wait.until(ExpectedConditions.stalenessOf(cartTable));
            cartProducts = wd.findElements(By.cssSelector("ul.items > li"));
        }
    }

}
