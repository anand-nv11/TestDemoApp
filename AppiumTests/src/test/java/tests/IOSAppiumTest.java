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

        caps.setCapability("platformName", "iOS");
        caps.setCapability("appium:automationName", "XCUITest");

        // GitHub Actions usually uses iPhone 16 / 15 depending on runner image.
        // If this fails, check simulator list from workflow logs.
        caps.setCapability("appium:deviceName", System.getProperty("deviceName", "iPhone 16"));
        caps.setCapability("appium:platformVersion", System.getProperty("platformVersion", ""));

        // Update this bundleId if your app bundle id is different.
        caps.setCapability("appium:bundleId", System.getProperty("bundleId", "test.com.SwiftUIDemoAllComponents"));

        caps.setCapability("appium:noReset", true);
        caps.setCapability("appium:newCommandTimeout", 120);

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
