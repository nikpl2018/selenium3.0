package litecart.main.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

public class MainPage {

    @FindBy(css = "ul.products > li")
    private List<WebElement> products;

    private final WebDriver wd;

    public MainPage(WebDriver wd) {
        PageFactory.initElements(wd, this);
        this.wd = wd;
    }

    public void open() {
        wd.get("http://localhost/litecart/");
    }

    public void selectProduct(int productIndex) {
        if (products.size() < productIndex)
            throw new IllegalStateException("В магазине отсутствует товар с индексом: " + productIndex);
        products.get(productIndex - 1).click();
    }

}
