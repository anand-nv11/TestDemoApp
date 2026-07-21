package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginPage {

    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(30);
    private static final Duration RETRY_DELAY = Duration.ofSeconds(2);
    private static final int MAX_LOGIN_RETRIES = 3;

    private final IOSDriver driver;
    private final WebDriverWait wait;

    private final By emailFieldLocator =
            AppiumBy.accessibilityId("loginEmailField");

    private final By passwordFieldLocator =
            AppiumBy.accessibilityId("loginPasswordField");

    private final By loginButtonLocator =
            AppiumBy.accessibilityId("loginButton");

    private final By loginErrorLocator =
            AppiumBy.accessibilityId("loginErrorMessage");

    private final By homeScreenLocator =
            AppiumBy.iOSNsPredicateString(
                    "name == 'Components' OR " +
                            "label == 'Components' OR " +
                            "name == 'homeScreen' OR " +
                            "label == 'homeScreen'"
            );

    private final By rememberMeToggleLocator =
            AppiumBy.accessibilityId("rememberMeToggle");

    private final By showPasswordButtonLocator =
            AppiumBy.accessibilityId("showPasswordButton");

    public LoginPage(IOSDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("IOSDriver cannot be null");
        }
        this.driver = driver;
        this.wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    }

    private WebElement waitForPresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    private WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private WebElement waitForClickable(By locator) {
        try {
            return wait.until(currentDriver -> {
                try {
                    WebElement element = currentDriver.findElement(locator);
                    return element.isDisplayed() && element.isEnabled() ? element : null;
                } catch (NoSuchElementException | StaleElementReferenceException ignored) {
                    return null;
                }
            });
        } catch (TimeoutException error) {
            logAvailableButtons(locator);
            throw error;
        }
    }

    private void safeClick(By locator) {
        Exception lastError = null;
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                WebElement element = waitForClickable(locator);
                element.click();
                return;
            } catch (Exception error) {
                lastError = error;
                try { driver.hideKeyboard(); } catch (Exception ignored) { }

                if (attempt == 3) {
                    try {
                        WebElement element = driver.findElement(locator);
                        int centerX = element.getLocation().getX() + element.getSize().getWidth() / 2;
                        int centerY = element.getLocation().getY() + element.getSize().getHeight() / 2;
                        Map<String, Object> arguments = new HashMap<>();
                        arguments.put("x", centerX);
                        arguments.put("y", centerY);
                        driver.executeScript("mobile: tap", arguments);
                        return;
                    } catch (Exception fallbackError) {
                        System.err.println("Coordinate tap failed: " + fallbackError.getMessage());
                    }
                }

                pause(Duration.ofMillis(500));
            }
        }
        throw new RuntimeException("Unable to click element: " + locator, lastError);
    }

    private void logAvailableButtons(By failedLocator) {
        System.err.println("Timed out waiting for clickable element: " + failedLocator);
        try {
            List<WebElement> buttons = driver.findElements(AppiumBy.className("XCUIElementTypeButton"));
            for (WebElement button : buttons) {
                System.err.println("Button name='" + safeAttribute(button, "name") +
                        "', label='" + safeAttribute(button, "label") + "'");
            }
        } catch (Exception error) {
            System.err.println("Unable to inspect buttons: " + error.getMessage());
        }
    }

    private String safeAttribute(WebElement element, String attribute) {
        try {
            String value = element.getAttribute(attribute);
            return value == null ? "" : value;
        } catch (Exception ignored) {
            return "";
        }
    }

    public void enterEmail(String email) {
        WebElement emailField = waitForVisible(emailFieldLocator);
        emailField.clear();
        emailField.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement passwordField = waitForVisible(passwordFieldLocator);
        passwordField.clear();
        passwordField.sendKeys(password);
        try { driver.hideKeyboard(); } catch (Exception ignored) { }
    }

    public void tapLogin() {
        safeClick(loginButtonLocator);
        waitForHomeScreenAppearance();
    }

    public void login(String email, String password) {
        for (int attempt = 1; attempt <= MAX_LOGIN_RETRIES; attempt++) {
            try {
                enterEmail(email);
                enterPassword(password);
                tapLogin();
                if (!isHomeScreenVisible()) {
                    System.out.println(driver.getPageSource());
                    throw new TimeoutException("Home screen was not loaded after login");
                }
                return;
            } catch (TimeoutException | NoSuchElementException | StaleElementReferenceException error) {
                if (attempt == MAX_LOGIN_RETRIES) {
                    captureDiagnostics("login-failed-after-retries");
                    throw error;
                }
                resetLoginFields();
                pause(RETRY_DELAY);
            }
        }
    }

    private void waitForHomeScreenAppearance() {
        wait.until(driver ->
                !driver.findElements(AppiumBy.accessibilityId("homeScreen")).isEmpty()
                        ||
                        !driver.findElements(
                                AppiumBy.iOSNsPredicateString(
                                        "name == 'Components' OR label == 'Components'"
                                )
                        ).isEmpty()
        );
    }

    private void resetLoginFields() {
        clearIfPresent(emailFieldLocator);
        clearIfPresent(passwordFieldLocator);
    }

    private void clearIfPresent(By locator) {
        try {
            List<WebElement> elements = driver.findElements(locator);
            if (!elements.isEmpty()) elements.get(0).clear();
        } catch (Exception ignored) { }
    }

    private void pause(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException error) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Wait interrupted", error);
        }
    }

    public String getErrorMessage() {
        return waitForVisible(loginErrorLocator).getText();
    }

    public boolean isLoginButtonEnabled() {
        try {
            return waitForPresent(loginButtonLocator).isEnabled();
        } catch (TimeoutException error) {
            return false;
        }
    }

    public boolean isLoginScreenVisible() {
        List<WebElement> emailFields = driver.findElements(emailFieldLocator);
        return !emailFields.isEmpty() && emailFields.get(0).isDisplayed();
    }

    public boolean isHomeScreenVisible() {
        try {
            return waitForVisible(homeScreenLocator).isDisplayed();
        } catch (TimeoutException error) {
            captureDiagnostics("components-tab-not-found");
            return false;
        }
    }

    public void toggleRememberMe() {
        safeClick(rememberMeToggleLocator);
    }

    public void togglePasswordVisibility() {
        safeClick(showPasswordButtonLocator);
    }

    private void captureDiagnostics(String name) {
        try {
            Path directory = Path.of("screenshots");
            Files.createDirectories(directory);
            long timestamp = System.currentTimeMillis();
            File screenshot = driver.getScreenshotAs(OutputType.FILE);
            Files.copy(screenshot.toPath(),
                    directory.resolve(name + "-" + timestamp + ".png"),
                    StandardCopyOption.REPLACE_EXISTING);
            Files.writeString(
                    directory.resolve(name + "-" + timestamp + "-page-source.xml"),
                    driver.getPageSource()
            );
        } catch (Exception error) {
            System.err.println("Unable to capture diagnostics: " + error.getMessage());
        }
    }
}
