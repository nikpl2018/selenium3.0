package litecart.main;

import base.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.BrowserType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static litecart.main.OpenCorrectPage.RGBAComponents.*;
import static org.hamcrest.core.Is.is;

public class OpenCorrectPage extends BaseTest {

    protected static enum RGBAComponents {
        RED,
        GREEN,
        BLUE,
        ALFA
    }

    @Test
    public void openCorrectPage() {
        checkProductCorrectPage();
        changeBrowser(BrowserType.FIREFOX);
        checkProductCorrectPage();
        changeBrowser(BrowserType.IE);
        checkProductCorrectPage();
    }

    private void checkProductCorrectPage() {
        wd.get("http://localhost/litecart/");
        List<WebElement> campaignsProducts = getCampaignsProducts();
        if (campaignsProducts.size() < 1)
            throw  new IllegalStateException("Товары не найдены");
        WebElement firstProduct = campaignsProducts.get(0);
        String mainPageProductName = getMainPageProductName(firstProduct);
        WebElement regularPriceWe = getRegularPriceWe(firstProduct);
        Assert.assertThat(regularPriceWe.getTagName(), equalTo("s"));
        List<Integer> regularPriceColorComponents = getRGBAColorComponents(regularPriceWe);
        Assert.assertThat(regularPriceColorComponents.get(RED.ordinal()), equalTo(regularPriceColorComponents.get(GREEN.ordinal())));
        Assert.assertThat(regularPriceColorComponents.get(GREEN.ordinal()), equalTo(regularPriceColorComponents.get(BLUE.ordinal())));
        String mainPageProductRegularPrice = regularPriceWe.getText();
        WebElement campaignPriceWe = getCampaignPrice(firstProduct);
        Assert.assertThat(campaignPriceWe.getTagName(), equalTo("strong"));
        List<Integer> campaignPriceColorComponents = getRGBAColorComponents(campaignPriceWe);
        Assert.assertThat(campaignPriceColorComponents.get(GREEN.ordinal()), equalTo(0));
        Assert.assertThat(campaignPriceColorComponents.get(BLUE.ordinal()), equalTo(0));
        String mainPageCampaignPrice = campaignPriceWe.getText();
        Assert.assertThat(campaignPriceWe.getSize().getHeight() > regularPriceWe.getSize().getHeight(), is(true));
        Assert.assertThat(campaignPriceWe.getSize().getWidth() > regularPriceWe.getSize().getWidth(), is(true));
        firstProduct.click();
        firstProduct = getProguct();
        String productPageProductName = getProductPageProductName(firstProduct);
        Assert.assertThat(mainPageProductName, equalTo(productPageProductName));
        regularPriceWe = getRegularPriceWe(firstProduct);
        Assert.assertThat(regularPriceWe.getTagName(), equalTo("s"));
        regularPriceColorComponents = getRGBAColorComponents(regularPriceWe);
        Assert.assertThat(regularPriceColorComponents.get(RED.ordinal()), equalTo(regularPriceColorComponents.get(GREEN.ordinal())));
        Assert.assertThat(regularPriceColorComponents.get(GREEN.ordinal()), equalTo(regularPriceColorComponents.get(BLUE.ordinal())));
        String productPageProductRegularPrice = regularPriceWe.getText();
        Assert.assertThat(mainPageProductRegularPrice, equalTo(productPageProductRegularPrice));
        campaignPriceWe = getCampaignPrice(firstProduct);
        Assert.assertThat(campaignPriceWe.getTagName(), equalTo("strong"));
        campaignPriceColorComponents = getRGBAColorComponents(campaignPriceWe);
        Assert.assertThat(campaignPriceColorComponents.get(GREEN.ordinal()), equalTo(0));
        Assert.assertThat(campaignPriceColorComponents.get(BLUE.ordinal()), equalTo(0));
        String productPageCampaignPrice = campaignPriceWe.getText();
        Assert.assertThat(campaignPriceWe.getSize().getHeight() > regularPriceWe.getSize().getHeight(), is(true));
        Assert.assertThat(campaignPriceWe.getSize().getWidth() > regularPriceWe.getSize().getWidth(), is(true));
        Assert.assertThat(mainPageCampaignPrice, equalTo(productPageCampaignPrice));
    }

    private List<WebElement> getCampaignsProducts() {
        return wd.findElements(By.cssSelector("div#box-campaigns ul > li"));
    }

    private String getMainPageProductName(WebElement product) {
        return product.findElement(By.cssSelector("div.name")).getText();
    }

    private WebElement getRegularPriceWe(WebElement product) {
        return product.findElement(By.cssSelector("s.regular-price"));
    }

    private WebElement getCampaignPrice(WebElement product) {
        return product.findElement(By.cssSelector(".campaign-price"));
    }

    private List<Integer> getRGBAColorComponents(WebElement element) {
        String color = element.getCssValue("color");
        if (color.contains("rgba(")) {
            return Arrays.stream(color.replace("rgba(", "").replace(")", "").split(", "))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } else if (color.contains("rgb(")) {
            return Arrays.stream(color.replace("rgb(", "").replace(")", "").split(", "))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } else {
            throw new IllegalStateException("Некорректный формат цвета");
        }
    }

    private WebElement getProguct() {
        return wd.findElement(By.cssSelector("div#box-product"));
    }

    private String getProductPageProductName(WebElement product) {
        return product.findElement(By.cssSelector("h1.title")).getText();
    }

}
