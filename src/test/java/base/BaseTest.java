package base;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BaseTest {
    protected WebDriver wd;

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
        wd.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @After
    public void stop() {
        wd.quit();
    }
}
