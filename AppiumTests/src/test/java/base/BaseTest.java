package base;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.ScreenshotUtil;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

public class BaseTest {

    protected IOSDriver driver;

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws Exception {

        String serverUrl = System.getProperty(
                "appiumServerUrl",
                "http://127.0.0.1:4723"
        );

        String bundleId = System.getProperty(
                "bundleId",
                "test.com.TodoApp"
        );

        String udid = System.getProperty("udid");

        String deviceName = System.getProperty(
                "deviceName",
                "iPhone 17 Pro"
        );

        String platformVersion = System.getProperty(
                "platformVersion",
                "26.4"
        );

        if (udid == null || udid.isBlank()) {
            throw new IllegalArgumentException(
                    "Simulator UDID is missing. Pass -Dudid=YOUR_SIMULATOR_UDID"
            );
        }

        XCUITestOptions options = new XCUITestOptions()
                .setDeviceName(deviceName)
                .setPlatformVersion(platformVersion)
                .setUdid(udid)
                .setBundleId(bundleId)
                .setNoReset(true)
                .setNewCommandTimeout(Duration.ofSeconds(300))
                .setWdaLaunchTimeout(Duration.ofSeconds(180))
                .setWdaConnectionTimeout(Duration.ofSeconds(180));

        URL appiumUrl = URI.create(serverUrl).toURL();

        System.out.println("==================================");
        System.out.println("Appium Server : " + appiumUrl);
        System.out.println("Bundle ID     : " + bundleId);
        System.out.println("Device Name   : " + deviceName);
        System.out.println("Platform      : " + platformVersion);
        System.out.println("UDID          : " + udid);
        System.out.println("==================================");

        driver = new IOSDriver(appiumUrl, options);

        driver.manage()
                .timeouts()
                .implicitlyWait(Duration.ofSeconds(10));

        System.out.println(
                "Appium Session Created : "
                        + driver.getSessionId()
        );
    }

    /**
     * Used by TestListener
     */
    public IOSDriver getDriver() {
        return driver;
    }

    @Attachment(value = "Failure Screenshot", type = "image/png")
    public byte[] attachScreenshot() {

        if (driver == null) {
            return new byte[0];
        }

        return driver.getScreenshotAs(OutputType.BYTES);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {

        try {

            if (driver != null && !result.isSuccess()) {

                File screenshot =
                        driver.getScreenshotAs(OutputType.FILE);

                ScreenshotUtil.saveScreenshot(
                        screenshot,
                        result.getName()
                );

                attachScreenshot();

                System.out.println(
                        "Screenshot captured for : "
                                + result.getName()
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Screenshot capture failed : "
                            + e.getMessage()
            );

        } finally {

            if (driver != null) {

                try {
                    driver.quit();
                } catch (Exception ignored) {
                }

                driver = null;
            }
        }
    }
}