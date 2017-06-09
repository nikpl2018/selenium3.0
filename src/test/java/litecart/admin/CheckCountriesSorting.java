package litecart.admin;

import base.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;

public class CheckCountriesSorting extends BaseTest {

    @Test
    public void checkCountriesSorting() {
        wd.get("http://localhost/litecart/admin/");
        adminLogin("admin", "admin");
        wd.get("http://localhost/litecart/admin/?app=countries&doc=countries");
        int countryNameColumnIndex = getColumnIndexByName("Name");
        List<String> countryNames = getCountryNames(countryNameColumnIndex);
        List<String> countryNamesSorted =  countryNames.stream().sorted().collect(Collectors.toList());
        Assert.assertThat(countryNames, equalTo(countryNamesSorted));
        int countryZonesColumnIndex = getColumnIndexByName("Zones");
        List<String> countryWithZonesUrls = getRows().stream()
            .filter(row -> isCountyWithZones(row, countryZonesColumnIndex))
            .map(row -> getCountryUrl(row, countryNameColumnIndex))
            .collect(Collectors.toList());
        countryWithZonesUrls.stream()
            .peek(wd::get)
            .forEach(this::checkZonesSorting);
        WebElement logout = wd.findElement(By.xpath("//a[@title='Logout']"));
        logout.click();
    }

    @Test
    public void checkGeoZones() {
        wd.get("http://localhost/litecart/admin/");
        adminLogin("admin", "admin");
        wd.get("http://localhost/litecart/admin/?app=geo_zones&doc=geo_zones");
        int countryNameColumnIndex = getColumnIndexByName("Name");
        List<String> countryWithZonesUrls = getRows().stream()
                .map(row -> getCountryUrl(row, countryNameColumnIndex))
                .collect(Collectors.toList());
        countryWithZonesUrls.stream()
                .peek(wd::get)
                .forEach(this::checkSelectedZonesSorting);
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

    private List<String> getCountryNames(int countryNameColumnIndex) {
        return wd.findElements(By.cssSelector("tr.row > th:nth-child(" + countryNameColumnIndex + ")")).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    private List<WebElement> getRows() {
        return wd.findElements(By.cssSelector("tr.row"));
    }

    private boolean isCountyWithZones(WebElement row, int countryZonesColumnIndex) {
        return Integer.parseInt(row.findElement(By.cssSelector("td:nth-child(" + countryZonesColumnIndex + ")")).getText()) > 0;
    }

    private String getCountryUrl(WebElement row, int countryNameColumnIndex) {
        return row.findElement(By.cssSelector("td:nth-child(" + countryNameColumnIndex + ") > a")).getAttribute("href");
    }

    private void checkZonesSorting(String url) {
        int zoneNameColumnIndex = getColumnIndexByName("Name");
        List<String> zoneNames = getZoneNames(zoneNameColumnIndex);
        List<String> zoneNamesSorted =  zoneNames.stream().sorted().collect(Collectors.toList());
        Assert.assertThat(zoneNames, equalTo(zoneNamesSorted));
    }

    private List<String> getZoneNames(int zoneNameColumnIndex) {
        return wd.findElements(By.xpath("//tr[./td/input[@type='hidden']]/td[" + zoneNameColumnIndex + "]")).stream().map(WebElement::getText).collect(Collectors.toList());
    }

    private void checkSelectedZonesSorting(String url) {
        int zoneNameColumnIndex = getColumnIndexByName("Zone");
        List<String> zoneNames = getSelectedZoneNames(zoneNameColumnIndex);
        List<String> zoneNamesSorted =  zoneNames.stream().sorted().collect(Collectors.toList());
        Assert.assertThat(zoneNames, equalTo(zoneNamesSorted));
    }

    private List<String> getSelectedZoneNames(int zoneNameColumnIndex) {
        return wd.findElements(By.xpath("//tr[./td/input[@type='hidden']]/td[" + zoneNameColumnIndex + "]//option[@selected]")).stream().map(WebElement::getText).collect(Collectors.toList());
    }
}
