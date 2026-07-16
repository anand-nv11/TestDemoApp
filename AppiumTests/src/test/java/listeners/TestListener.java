package listeners;

import base.BaseTest;
import io.appium.java_client.ios.IOSDriver;
import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TestListener implements ITestListener {

    private static final Path SCREENSHOT_DIRECTORY =
            Path.of("screenshots");

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println(
                "START TEST: " + result.getName()
        );
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println(
                "PASSED TEST: " + result.getName()
        );
    }

    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println(
                "FAILED TEST: " + result.getName()
        );

        if (result.getThrowable() != null) {
            result.getThrowable().printStackTrace();
        }

        captureFailureScreenshot(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println(
                "SKIPPED TEST: " + result.getName()
        );

        if (result.getThrowable() != null) {
            result.getThrowable().printStackTrace();
        }
    }

    private void captureFailureScreenshot(
            ITestResult result
    ) {
        try {
            Object testInstance = result.getInstance();

            if (!(testInstance instanceof BaseTest)) {
                System.err.println(
                        "Screenshot skipped: test class does not extend BaseTest."
                );
                return;
            }

            BaseTest baseTest = (BaseTest) testInstance;
            IOSDriver driver = baseTest.getDriver();

            if (driver == null) {
                System.err.println(
                        "Screenshot skipped: IOSDriver is null."
                );
                return;
            }

            Files.createDirectories(SCREENSHOT_DIRECTORY);

            String timestamp = LocalDateTime.now()
                    .format(
                            DateTimeFormatter.ofPattern(
                                    "yyyyMMdd-HHmmss"
                            )
                    );

            String safeTestName = result.getName()
                    .replaceAll("[^a-zA-Z0-9-_]", "_");

            String screenshotName =
                    safeTestName + "-" + timestamp + ".png";

            Path destination =
                    SCREENSHOT_DIRECTORY.resolve(
                            screenshotName
                    );

            File sourceFile =
                    ((TakesScreenshot) driver)
                            .getScreenshotAs(
                                    OutputType.FILE
                            );

            Files.copy(
                    sourceFile.toPath(),
                    destination,
                    StandardCopyOption.REPLACE_EXISTING
            );

            System.out.println(
                    "Failure screenshot saved: "
                            + destination.toAbsolutePath()
            );

            attachScreenshotToAllure(
                    driver,
                    screenshotName
            );

        } catch (Exception error) {
            System.err.println(
                    "Unable to capture failure screenshot: "
                            + error.getMessage()
            );

            error.printStackTrace();
        }
    }

    private void attachScreenshotToAllure(
            IOSDriver driver,
            String screenshotName
    ) {
        try {
            byte[] screenshotBytes =
                    ((TakesScreenshot) driver)
                            .getScreenshotAs(
                                    OutputType.BYTES
                            );

            Allure.addAttachment(
                    screenshotName,
                    "image/png",
                    new ByteArrayInputStream(
                            screenshotBytes
                    ),
                    ".png"
            );

            System.out.println(
                    "Screenshot attached to Allure report."
            );

        } catch (Exception error) {
            System.err.println(
                    "Unable to attach screenshot to Allure: "
                            + error.getMessage()
            );
        }
    }
}