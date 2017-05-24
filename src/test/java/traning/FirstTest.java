package traning;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.Map;

public class FirstTest {

    private WebDriver wd;

    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"/src/test/resources/webdrivers/chromedriver.exe");
        Map<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("credentials_enable_service", false);
        chromePrefs.put("disable-popup-blocking", "true");
        chromePrefs.put("download.prompt_for_download", "false");
        chromePrefs.put("profile.default_content_setting_values.notifications", 2);
        chromePrefs.put("directory_upgrade", true);
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", chromePrefs);
        options.addArguments("no-sandbox");
        options.addArguments("disable-infobars");
        options.addArguments("--disable-print-preview");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        wd = new ChromeDriver(capabilities);
    }

    @After
    public void stop() {
        wd.quit();
    }

    @Test
    public void openYandex() {
        wd.get("http://www.ya.ru");
    }
}
