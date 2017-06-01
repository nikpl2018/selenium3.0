package litecart.main;

import base.BaseTest;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CheckSticker extends BaseTest {

    @Test
    public void shouldProductHasOnlyOneSticker() {
        wd.get("http://localhost/litecart/");
        List<WebElement> producs = wd.findElements(By.cssSelector("ul.products > li"));
        producs.forEach(this::hasOnlyOneSticker);
    }

    private void hasOnlyOneSticker(WebElement product) {
        if (product.findElements(By.cssSelector("div.sticker")).size() != 1)
            throw new IllegalStateException("Продукт не имеет ровно 1 стикер");
    }

}
