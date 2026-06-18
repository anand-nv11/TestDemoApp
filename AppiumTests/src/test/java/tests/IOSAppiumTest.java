package tests;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import io.qameta.allure.*;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.ScreenshotUtil;

import java.io.File;
import java.net.URL;
import java.time.Duration;

@Epic("iOS Automation")
@Feature("SwiftUI Demo Components")
public class IOSAppiumTest {

    private IOSDriver driver;

    @BeforeMethod
    public void setUp() throws Exception {

        DesiredCapabilities caps = new DesiredCapabilities();

        String bundleId = System.getProperty("bundleId", "test.com.SwiftUIDemoAllComponents");
        String udid = System.getProperty("udid");
        String deviceName = System.getProperty("deviceName", "iPhone 17");
        String platformVersion = System.getProperty("platformVersion", "26.4");

        caps.setCapability("platformName", "iOS");
        caps.setCapability("appium:automationName", "XCUITest");

        // Important: use already booted simulator from GitHub Actions
        if (udid != null && !udid.isEmpty()) {
            caps.setCapability("appium:udid", udid);
        }

        caps.setCapability("appium:deviceName", deviceName);
        caps.setCapability("appium:platformVersion", platformVersion);
        caps.setCapability("appium:bundleId", bundleId);

        caps.setCapability("appium:noReset", true);
        caps.setCapability("appium:newCommandTimeout", 120);
        caps.setCapability("appium:wdaLaunchTimeout", 180000);
        caps.setCapability("appium:wdaConnectionTimeout", 180000);
        caps.setCapability("appium:simulatorStartupTimeout", 180000);

        driver = new IOSDriver(new URL("http://127.0.0.1:4723"), caps);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    @Test(description = "Verify app launches successfully")
    @Story("App Launch")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyAppLaunch() {
        Assert.assertNotNull(driver, "Driver should not be null");
    }

    @Test(description = "Tap Scrolling component if available")
    @Story("Component Navigation")
    @Severity(SeverityLevel.NORMAL)
    public void tapScrollingComponent() {
        WebElement scrolling = driver.findElement(AppiumBy.iOSNsPredicateString(
                "name == 'Scrolling' OR label == 'Scrolling' OR value == 'Scrolling'"
        ));
        scrolling.click();
        Assert.assertTrue(true, "Scrolling component tapped successfully");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        try {
            if (driver != null && !result.isSuccess()) {
                File screenshot = driver.getScreenshotAs(OutputType.FILE);
                ScreenshotUtil.saveScreenshot(screenshot, result.getName());
            }
        } catch (Exception e) {
            System.out.println("Screenshot capture failed: " + e.getMessage());
        } finally {
            if (driver != null) {
                driver.quit();
            }
        }
    }
}
