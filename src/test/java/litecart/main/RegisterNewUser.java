package litecart.main;

import base.BaseTest;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.UUID;

public class RegisterNewUser extends BaseTest {

    @Test
    public void registerNewUserTest() {
        wd.get("http://localhost/litecart/");
        User user = new User(
                "rgfcvgtfv",
                "rfcgtgvc",
                "frcfrc",
                45434,
                "gvfgtv",
                "United States",
                "Arkansas",
                randomString(15) + "@1.ru",
                "5555555",
                "Qwerty123");
        registerNewUser(user);
        logout();
        login(user.getEmail(), user.getPassword());
        logout();
    }

    private String randomString(int length) {
        if (length < 1 || length > 32)
            throw  new IllegalArgumentException("Некорректная длина строки");
        String raw = UUID.randomUUID().toString().replaceAll("-", "");
        return raw.substring(0, length);
    }

    private void registerNewUser(User user) {
        WebElement newUserLink = wd.findElement(By.xpath("//a[text() = 'New customers click here']"));
        newUserLink.click();
        WebElement firstNameField = wd.findElement(By.cssSelector("input[name = 'firstname']"));
        firstNameField.sendKeys(user.getFirstName());
        WebElement lastNameField = wd.findElement(By.cssSelector("input[name = 'lastname']"));
        lastNameField.sendKeys(user.getLastName());
        WebElement address1Field = wd.findElement(By.cssSelector("input[name = 'address1']"));
        address1Field.sendKeys(user.getAddress1());
        WebElement postcodeField = wd.findElement(By.cssSelector("input[name = 'postcode']"));
        postcodeField.sendKeys(String.valueOf(user.getPostcode()));
        WebElement cityField = wd.findElement(By.cssSelector("input[name = 'city']"));
        cityField.sendKeys(user.getCity());
        WebElement countryField = wd.findElement(By.cssSelector("span[id *= 'select2-country_code']"));
        countryField.click();
        WebElement countryFilterField = wd.findElement(By.cssSelector("input.select2-search__field"));
        countryFilterField.sendKeys(user.getCountry());
        List<WebElement> countriesList = wd.findElements(By.cssSelector("ul[id *= 'select2-country_code'] > li"));
        WebElement countryListOption = countriesList.stream()
                .filter(we -> user.getCountry().equals(we.getText()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Страна не найдена " + user.getCountry()));
        countryListOption.click();
        WebElement stateSelectWe = wd.findElement(By.cssSelector("select[name = 'zone_code']"));
        Select stateSelect = new Select(stateSelectWe);
        stateSelect.selectByVisibleText(user.getState());
        WebElement emailField = wd.findElement(By.cssSelector("input[name = 'email']"));
        emailField.sendKeys(user.getEmail());
        WebElement phoneField = wd.findElement(By.cssSelector("input[name = 'phone']"));
        phoneField.sendKeys(user.getPhone());
        WebElement passwordField = wd.findElement(By.cssSelector("input[name = 'password']"));
        passwordField.sendKeys(user.getPassword());
        WebElement confirmedPasswordField = wd.findElement(By.cssSelector("input[name = 'confirmed_password']"));
        confirmedPasswordField.sendKeys(user.getPassword());
        WebElement createButton = wd.findElement(By.cssSelector("button[name = 'create_account']"));
        createButton.click();
    }

    private void logout() {
        WebElement logout = wd.findElement(By.xpath("//a[text() = 'Logout']"));
        logout.click();
    }

    private void login(String email, String password) {
        WebElement emailField = wd.findElement(By.cssSelector("input[name = 'email']"));
        emailField.sendKeys(email);
        WebElement passwordField = wd.findElement(By.cssSelector("input[name = 'password']"));
        passwordField.sendKeys(password);
        WebElement login = wd.findElement(By.cssSelector("button[name = 'login']"));
        login.click();
    }

    private static class User {
        private final String firstName;
        private final String lastName;
        private final String address1;
        private final int postcode;
        private final String city;
        private final String country;
        private final String state;
        private final String email;
        private final String phone;
        private final String password;

        public User(String firstName, String lastName, String address1, int postcode, String city, String country, String state, String email, String phone, String password) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.address1 = address1;
            this.postcode = postcode;
            this.city = city;
            this.country = country;
            this.state = state;
            this.email = email;
            this.phone = phone;
            this.password = password;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getAddress1() {
            return address1;
        }

        public int getPostcode() {
            return postcode;
        }

        public String getCity() {
            return city;
        }

        public String getCountry() {
            return country;
        }

        public String getState() {
            return state;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public String getPassword() {
            return password;
        }
    }

}
