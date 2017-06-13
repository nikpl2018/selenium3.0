package litecart.admin;

import base.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.math.BigDecimal;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;

public class AddNewProduct extends BaseTest {

    @Test
    public void addNewProductTest() {
        wd.get("http://localhost/litecart/admin/");
        adminLogin("admin", "admin");
        clickOnMenuItem("Catalog");
        WebElement addProductButton = wd.findElement(By.xpath("//a[contains(., 'Add New Product')]"));
        addProductButton.click();
        Product product = new Product(
                true,
                "Super product",
                "007",
                EnumSet.of(Gender.MALE, Gender.FEMALE),
                10,
                "super_product.jpg",
                "ACME Corp.",
                "product",
                "Super product",
                "Super super product",
                "Super product",
                "Super product",
                new BigDecimal(10),
                "US Dollars",
                new BigDecimal(15),
                new BigDecimal(12)
        );
        addNewProduct(product);
        int productNameColumnIndex = getColumnIndexByName("Name");
        List<String> productNames = getProductNames(productNameColumnIndex);
        Assert.assertThat(productNames, hasItem(product.getName()));
        WebElement logout = wd.findElement(By.xpath("//a[@title='Logout']"));
        logout.click();
    }

    private void clickOnMenuItem(String itemName) {
        wd.findElement(By.xpath(String.format("//ul[@id='box-apps-menu']/li[contains(., '%s')]", itemName))).click();
    }

    private void switchTab(String tabName) {
        wd.findElement(By.xpath(String.format("//a[contains(@href,'#tab-') and contains(., '%s')]", tabName))).click();
    }

    private void addNewProduct(Product product) {
        setStatus(product.isStatus());
        WebElement nameField = wd.findElement(By.cssSelector("input[name = 'name[en]']"));
        nameField.sendKeys(product.getName());
        WebElement codeField = wd.findElement(By.cssSelector("input[name = 'code']"));
        codeField.sendKeys(product.getCode());
        setGender(EnumSet.of(Gender.MALE, Gender.FEMALE));
        WebElement quantityField = wd.findElement(By.cssSelector("input[name = 'quantity']"));
        quantityField.clear();
        quantityField.sendKeys(String.valueOf(product.getQuantity()));
        WebElement imageField = wd.findElement(By.cssSelector("input[name = 'new_images[]']"));
        imageField.sendKeys(System.getProperty("user.dir").replace('\\','/') + "/src/test/resources/image/" + product.getImageFileName());
        switchTab("Information");
        sleep(500L);
        WebElement manufacturerField = wd.findElement(By.cssSelector("select[name = 'manufacturer_id']"));
        Select manufacturer = new Select(manufacturerField);
        manufacturer.selectByVisibleText(product.getManufacturer());
        WebElement keywordsField = wd.findElement(By.cssSelector("input[name = 'keywords']"));
        keywordsField.sendKeys(product.getKeywords());
        WebElement shortDescriptionField = wd.findElement(By.cssSelector("input[name = 'short_description[en]']"));
        shortDescriptionField.sendKeys(product.getShortDescription());
        WebElement descriptionField = wd.findElement(By.cssSelector("textarea[name = 'description[en]']"));
        descriptionField.sendKeys(product.getDescription());
        WebElement headTitleField = wd.findElement(By.cssSelector("input[name = 'head_title[en]']"));
        headTitleField.sendKeys(product.getHeadTitle());
        WebElement metaDescriptionField = wd.findElement(By.cssSelector("input[name = 'meta_description[en]']"));
        metaDescriptionField.sendKeys(product.getMetaDescription());
        switchTab("Prices");
        sleep(500L);
        WebElement purchasePriceField = wd.findElement(By.cssSelector("input[name = 'purchase_price']"));
        purchasePriceField.clear();
        purchasePriceField.sendKeys(product.getPurchasePrice().toString());
        WebElement purchasePriceCurrencyCodeField = wd.findElement(By.cssSelector("select[name = 'purchase_price_currency_code']"));
        Select purchasePriceCurrencyCode = new Select(purchasePriceCurrencyCodeField);
        purchasePriceCurrencyCode.selectByVisibleText(product.getPurchasePriceCurrency());
        WebElement pricesUSDField = wd.findElement(By.cssSelector("input[name = 'prices[USD]']"));
        pricesUSDField.sendKeys(product.getPricesUSD().toString());
        WebElement pricesEURField = wd.findElement(By.cssSelector("input[name = 'prices[EUR]']"));
        pricesEURField.sendKeys(product.getPricesEUR().toString());
        WebElement save = wd.findElement(By.cssSelector("button[name = 'save']"));
        save.click();
    }

    private void setStatus(boolean enabled) {
        String status = enabled ? "Enabled" : "Disabled";
           wd.findElement(By.xpath(String.format("//label[./input[@name='status'] and contains(., '%s')]", status))).click();
    }

    private void setGender(Set<Gender> genders) {
        genders.forEach(gender -> wd.findElement(By.xpath(String.format("//input[./../following-sibling::td[text() = '%s'] and @name = 'product_groups[]']", gender.getTitle()))));
    }

    private int getColumnIndexByName(String name) {
        List<String> columnNames = wd.findElements(By.cssSelector("tr.header > th")).stream().map(WebElement::getText).collect(Collectors.toList());
        int columnIndex = columnNames.indexOf(name);
        if (columnIndex < 0)
            throw new IllegalArgumentException("Колонка с именем '" + name + "' не найдена");
        return columnIndex + 1;
    }

    private List<String> getProductNames(int productNameColumnIndex) {
        return wd.findElements(By.cssSelector("tr.row > td:nth-child(" + productNameColumnIndex + ")")).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    private void sleep(long ms) {
        try {
            TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static enum Gender {
        FEMALE("Female"),
        MALE("Male"),
        UNISEX("Unisex");

        private final String title;

        Gender(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }
    }

    private static class Product {

        private final boolean status;
        private final String name;
        private final String code;
        private final Set<Gender> genders;
        private final int quantity;
        private final String imageFileName;
        private final String manufacturer;
        private final String keywords;
        private final String shortDescription;
        private final String description;
        private final String headTitle;
        private final String metaDescription;
        private final BigDecimal purchasePrice;
        private final String purchasePriceCurrency;
        private final BigDecimal pricesUSD;
        private final BigDecimal pricesEUR;

        public Product(
                boolean status,
                String name,
                String code,
                Set<Gender> genders,
                int quantity,
                String imageFileName,
                String manufacturer,
                String keywords,
                String shortDescription,
                String description,
                String headTitle,
                String metaDescription,
                BigDecimal purchasePrice,
                String purchasePriceCurrency,
                BigDecimal pricesUSD,
                BigDecimal pricesEUR) {
            this.status = status;
            this.name = name;
            this.code = code;
            this.genders = genders;
            this.quantity = quantity;
            this.imageFileName = imageFileName;
            this.manufacturer = manufacturer;
            this.keywords = keywords;
            this.shortDescription = shortDescription;
            this.description = description;
            this.headTitle = headTitle;
            this.metaDescription = metaDescription;
            this.purchasePrice = purchasePrice;
            this.purchasePriceCurrency = purchasePriceCurrency;
            this.pricesUSD = pricesUSD;
            this.pricesEUR = pricesEUR;
        }

        public boolean isStatus() {
            return status;
        }

        public String getName() {
            return name;
        }

        public String getCode() {
            return code;
        }

        public Set<Gender> getGenders() {
            return genders;
        }

        public int getQuantity() {
            return quantity;
        }

        public String getImageFileName() {
            return imageFileName;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public String getKeywords() {
            return keywords;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public String getDescription() {
            return description;
        }

        public String getHeadTitle() {
            return headTitle;
        }

        public String getMetaDescription() {
            return metaDescription;
        }

        public BigDecimal getPurchasePrice() {
            return purchasePrice;
        }

        public String getPurchasePriceCurrency() {
            return purchasePriceCurrency;
        }

        public BigDecimal getPricesUSD() {
            return pricesUSD;
        }

        public BigDecimal getPricesEUR() {
            return pricesEUR;
        }
    }

}
