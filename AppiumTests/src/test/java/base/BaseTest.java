package base;

import io.appium.java_client.ios.IOSDriver;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.ScreenshotUtil;

import java.io.File;
import java.net.URL;
import java.time.Duration;

public class BaseTest {

    protected IOSDriver driver;

    @BeforeMethod
    public void setUp() throws Exception {
        DesiredCapabilities caps = new DesiredCapabilities();

        String bundleId = System.getProperty("bundleId", "test.com.SwiftUIDemoAllComponents");
        String udid = System.getProperty("udid");
        String deviceName = System.getProperty("deviceName", "iPhone 17");
        String platformVersion = System.getProperty("platformVersion", "26.4");

        caps.setCapability("platformName", "iOS");
        caps.setCapability("appium:automationName", "XCUITest");

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

    @Attachment(value = "Screenshot", type = "image/png")
    public byte[] attachScreenshot() {
        if (driver == null) return new byte[0];
        return driver.getScreenshotAs(OutputType.BYTES);
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        try {
            if (driver != null && !result.isSuccess()) {
                File screenshot = driver.getScreenshotAs(OutputType.FILE);
                ScreenshotUtil.saveScreenshot(screenshot, result.getName());
                attachScreenshot();
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