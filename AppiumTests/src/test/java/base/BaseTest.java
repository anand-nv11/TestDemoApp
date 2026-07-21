package base;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import utils.ScreenshotUtil;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.time.Duration;

public class BaseTest {

    protected IOSDriver driver;

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {

        String serverUrl = System.getProperty(
                "appiumServerUrl",
                "http://127.0.0.1:4723"
        );

        String bundleId = System.getProperty(
                "bundleId",
                "test.com.SwiftUIDemoAllComponents"
        );

        String udid = System.getProperty("udid");

        String deviceName = System.getProperty(
                "deviceName",
                "iPhone 17"
        );

        String platformVersion = System.getProperty(
                "platformVersion",
                "26.4"
        );

        boolean noReset = Boolean.parseBoolean(
                System.getProperty("noReset", "false")
        );

        boolean useNewWDA = Boolean.parseBoolean(
                System.getProperty("useNewWDA", "false")
        );

        boolean clearSystemFiles = Boolean.parseBoolean(
                System.getProperty("clearSystemFiles", "true")
        );

        boolean showXcodeLog = Boolean.parseBoolean(
                System.getProperty("showXcodeLog", "true")
        );

        long wdaLaunchTimeout = Long.parseLong(
                System.getProperty("wdaLaunchTimeout", "240000")
        );

        long wdaConnectionTimeout = Long.parseLong(
                System.getProperty("wdaConnectionTimeout", "240000")
        );

        int wdaStartupRetries = Integer.parseInt(
                System.getProperty("wdaStartupRetries", "4")
        );

        long wdaStartupRetryInterval = Long.parseLong(
                System.getProperty("wdaStartupRetryInterval", "20000")
        );

        if (udid == null || udid.isBlank()) {
            throw new IllegalArgumentException(
                    "Simulator UDID is missing. " +
                            "Pass -Dudid=YOUR_SIMULATOR_UDID"
            );
        }

        XCUITestOptions options = new XCUITestOptions()
                .setDeviceName(deviceName)
                .setPlatformVersion(platformVersion)
                .setUdid(udid)
                .setBundleId(bundleId)
                .setNoReset(noReset)
                .setNewCommandTimeout(Duration.ofSeconds(300))
                .setWdaLaunchTimeout(Duration.ofMillis(wdaLaunchTimeout))
                .setWdaConnectionTimeout(Duration.ofMillis(wdaConnectionTimeout));

        options.setCapability("appium:useNewWDA", useNewWDA);
        options.setCapability("appium:clearSystemFiles", clearSystemFiles);
        options.setCapability("appium:showXcodeLog", showXcodeLog);
        options.setCapability("appium:wdaStartupRetries", wdaStartupRetries);
        options.setCapability(
                "appium:wdaStartupRetryInterval",
                wdaStartupRetryInterval
        );

        URL appiumUrl = URI.create(serverUrl).toURL();

        System.out.println("==================================");
        System.out.println("Appium Server : " + appiumUrl);
        System.out.println("Bundle ID     : " + bundleId);
        System.out.println("Device Name   : " + deviceName);
        System.out.println("Platform      : " + platformVersion);
        System.out.println("UDID          : " + udid);
        System.out.println("noReset       : " + noReset);
        System.out.println("useNewWDA     : " + useNewWDA);
        System.out.println("WDA retries   : " + wdaStartupRetries);
        System.out.println("WDA timeout   : " + wdaLaunchTimeout + " ms");
        System.out.println("==================================");

        driver = new IOSDriver(
                appiumUrl,
                options
        );

        try {
            driver.activateApp(bundleId);
        } catch (Exception error) {
            System.err.println(
                    "App activation warning: " + error.getMessage()
            );
        }

        driver.manage()
                .timeouts()
                .implicitlyWait(Duration.ofSeconds(0));

        System.out.println(
                "Appium Session Created: " + driver.getSessionId()
        );
    }

    /**
     * Used by tests and TestListener.
     */
    public IOSDriver getDriver() {
        return driver;
    }

    @Attachment(
            value = "Failure Screenshot",
            type = "image/png"
    )
    public byte[] attachScreenshot() {
        if (driver == null) {
            return new byte[0];
        }

        return driver.getScreenshotAs(OutputType.BYTES);
    }

    @AfterMethod(alwaysRun = true)
    public void captureFailureScreenshot(ITestResult result) {
        if (result.isSuccess() || driver == null) {
            return;
        }

        try {
            File screenshot = driver.getScreenshotAs(OutputType.FILE);

            ScreenshotUtil.saveScreenshot(
                    screenshot,
                    result.getName()
            );

            attachScreenshot();

            System.out.println(
                    "Failure screenshot captured for: " + result.getName()
            );
        } catch (Exception error) {
            System.err.println(
                    "Screenshot capture failed: " + error.getMessage()
            );
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        if (driver == null) {
            return;
        }

        try {
            driver.quit();
            System.out.println("Appium session closed.");
        } catch (Exception error) {
            System.err.println(
                    "Unable to close Appium session: " + error.getMessage()
            );
        } finally {
            driver = null;
        }
    }
}