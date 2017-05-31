package base;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BaseTest {

    private static final String DRIVERS_PATH = System.getProperty("user.dir") + "/src/test/resources/webdrivers";

    protected WebDriver wd;

    @Before
    public void setup() {
        wd = wdFactory(BrowserType.CHROME);
        wd.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    private WebDriver wdFactory(String browser) {
        if (BrowserType.CHROME.equals(browser)) {
            System.setProperty("webdriver.chrome.driver", DRIVERS_PATH+"/chromedriver.exe");
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
            options.addArguments("--start-maximized");
            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            return new ChromeDriver(capabilities);
        } else if (BrowserType.FIREFOX.equals(browser)) {
            System.setProperty("webdriver.gecko.driver",DRIVERS_PATH+"/geckodriver.exe");
            return new FirefoxDriver();
        } else if (BrowserType.IE.equals(browser)) {
            System.setProperty("webdriver.ie.driver",DRIVERS_PATH+"/IEDriverServer.exe");
            return new InternetExplorerDriver();
        } else {
            throw new IllegalArgumentException("Неподдерживаемый браузер: " + browser);
        }
    }

    @After
    public void stop() {
        wd.quit();
    }
}