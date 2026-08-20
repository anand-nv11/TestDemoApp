package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
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

    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(40);
    private static final Duration SHORT_WAIT = Duration.ofSeconds(5);
    private static final Duration RETRY_DELAY = Duration.ofSeconds(2);
    private static final Duration ELEMENT_STABILIZE_DELAY = Duration.ofMillis(1000);
    private static final int MAX_LOGIN_RETRIES = 3;
    private static final int MAX_CLICK_RETRIES = 5;

    private final IOSDriver driver;
    private final WebDriverWait wait;
    private final WebDriverWait shortWait;

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
        this.shortWait = new WebDriverWait(driver, SHORT_WAIT);
    }

    /**
     * Wait for element presence in DOM
     */
    private WebElement waitForPresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Wait for element to be visible and rendered
     */
    private WebElement waitForVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Enhanced wait for element to be in a state suitable for interaction
     */
    private WebElement waitForInteractable(By locator) {
        return wait.until(driver -> {
            try {
                WebElement element = driver.findElement(locator);
<<<<<<< HEAD

=======
<<<<<<< HEAD
                
=======

>>>>>>> 0be48a8 (change login screen code and test case in java file .)
>>>>>>> 169c65d (change login screen code and test case in java file .)
                // Check if element is displayed
                if (!element.isDisplayed()) {
                    System.out.println("Element not displayed: " + locator);
                    return null;
                }

                // Check if element is enabled
                if (!element.isEnabled()) {
                    System.out.println("Element not enabled: " + locator);
                    return null;
                }

                // Verify element is in viewport with valid coordinates
                Rectangle rect = element.getRect();
                Point location = rect.getPoint();
                Dimension size = rect.getDimension();

                if (location.getX() < 0 || location.getY() < 0 ||
                        size.getWidth() <= 0 || size.getHeight() <= 0) {
<<<<<<< HEAD
                    System.out.println("Element has invalid coordinates: " + locator +
                            " at (" + location.getX() + ", " + location.getY() +
=======
<<<<<<< HEAD
                    System.out.println("Element has invalid coordinates: " + locator + 
                            " at (" + location.getX() + ", " + location.getY() + 
=======
                    System.out.println("Element has invalid coordinates: " + locator +
                            " at (" + location.getX() + ", " + location.getY() +
>>>>>>> 0be48a8 (change login screen code and test case in java file .)
>>>>>>> 169c65d (change login screen code and test case in java file .)
                            ") with size (" + size.getWidth() + ", " + size.getHeight() + ")");
                    return null;
                }

                return element;
            } catch (NoSuchElementException | StaleElementReferenceException e) {
                return null;
            }
        });
    }

    /**
     * Scroll element into view using JavaScript execution
     */
    private void scrollIntoView(WebElement element) {
        try {
            driver.executeScript("arguments[0].scrollIntoView(true);", element);
            pause(ELEMENT_STABILIZE_DELAY);
        } catch (Exception e) {
            System.out.println("Scroll into view failed (non-critical): " + e.getMessage());
        }
    }

    /**
     * Reliable click with multiple strategies and retries
     */
    private void reliableClick(By locator, String elementName) {
        Exception lastError = null;

        for (int attempt = 1; attempt <= MAX_CLICK_RETRIES; attempt++) {
            try {
                System.out.println("Attempting click on " + elementName + " (attempt " + attempt + "/" + MAX_CLICK_RETRIES + ")");

                // Wait for element to be interactable
                WebElement element = waitForInteractable(locator);

                // Ensure element is visible in viewport
                scrollIntoView(element);
<<<<<<< HEAD

=======
<<<<<<< HEAD
                
=======

>>>>>>> 0be48a8 (change login screen code and test case in java file .)
>>>>>>> 169c65d (change login screen code and test case in java file .)
                // Add stabilization delay
                pause(Duration.ofMillis(500));

                // Strategy 1: Standard WebElement click
                try {
                    element.click();
                    System.out.println("Successfully clicked " + elementName + " using WebElement.click()");
                    return;
                } catch (Exception e1) {
                    System.out.println("WebElement.click() failed: " + e1.getMessage());
                    lastError = e1;
                }

                // Strategy 2: Coordinate-based tap
                try {
                    Rectangle rect = element.getRect();
                    int centerX = rect.getPoint().getX() + rect.getDimension().getWidth() / 2;
                    int centerY = rect.getPoint().getY() + rect.getDimension().getHeight() / 2;

                    if (centerX > 0 && centerY > 0) {
                        Map<String, Object> tapArgs = new HashMap<>();
                        tapArgs.put("x", centerX);
                        tapArgs.put("y", centerY);
                        driver.executeScript("mobile: tap", tapArgs);
                        System.out.println("Successfully tapped " + elementName + " at coordinates (" + centerX + ", " + centerY + ")");
                        return;
                    }
                } catch (Exception e2) {
                    System.out.println("Coordinate tap failed: " + e2.getMessage());
                    lastError = e2;
                }

                // Strategy 3: Try to hide keyboard before retry
                if (attempt < MAX_CLICK_RETRIES) {
                    try {
                        driver.hideKeyboard();
                    } catch (Exception ignored) {
                    }

                    pause(Duration.ofMillis(800));
                }

            } catch (TimeoutException e) {
                lastError = e;
<<<<<<< HEAD
                System.out.println("Timeout waiting for element to be interactable: " + elementName +
                        " (attempt " + attempt + "/" + MAX_CLICK_RETRIES + ")");

=======
<<<<<<< HEAD
                System.out.println("Timeout waiting for element to be interactable: " + elementName + 
                        " (attempt " + attempt + "/" + MAX_CLICK_RETRIES + ")");
                
=======
                System.out.println("Timeout waiting for element to be interactable: " + elementName +
                        " (attempt " + attempt + "/" + MAX_CLICK_RETRIES + ")");

>>>>>>> 0be48a8 (change login screen code and test case in java file .)
>>>>>>> 169c65d (change login screen code and test case in java file .)
                if (attempt < MAX_CLICK_RETRIES) {
                    logPageSourceForDebug(elementName);
                    pause(Duration.ofMillis(500));
                }
            } catch (Exception e) {
                lastError = e;
                System.out.println("Unexpected error during click: " + e.getMessage());
                if (attempt < MAX_CLICK_RETRIES) {
                    pause(Duration.ofMillis(500));
                }
            }
        }

        captureDiagnostics("click-failed-" + elementName);
<<<<<<< HEAD
        throw new RuntimeException("Failed to click " + elementName + " after " + MAX_CLICK_RETRIES +
=======
<<<<<<< HEAD
        throw new RuntimeException("Failed to click " + elementName + " after " + MAX_CLICK_RETRIES + 
=======
        throw new RuntimeException("Failed to click " + elementName + " after " + MAX_CLICK_RETRIES +
>>>>>>> 0be48a8 (change login screen code and test case in java file .)
>>>>>>> 169c65d (change login screen code and test case in java file .)
                " attempts", lastError);
    }

    /**
     * Reliable text input with clear and verification
     */
    private void reliableInput(By locator, String elementName, String text) {
        Exception lastError = null;

        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                System.out.println("Attempting to enter text in " + elementName + " (attempt " + attempt + "/3)");

                WebElement element = waitForInteractable(locator);
                scrollIntoView(element);
                pause(Duration.ofMillis(500));

                // Clear the field
                element.clear();
                pause(Duration.ofMillis(300));

                // Send keys
                element.sendKeys(text);
                pause(Duration.ofMillis(500));

                // Verify text was entered
                String currentValue = element.getAttribute("value");
                if (currentValue == null || !currentValue.contains(text)) {
                    System.out.println("Text not properly entered. Expected: " + text + ", Got: " + currentValue);
                    lastError = new RuntimeException("Text input verification failed");
                    if (attempt < 3) {
                        pause(Duration.ofMillis(500));
                        continue;
                    }
                }

                System.out.println("Successfully entered text in " + elementName);
                return;

            } catch (TimeoutException e) {
                lastError = e;
                System.out.println("Timeout waiting for " + elementName);
                if (attempt < 3) {
                    pause(Duration.ofMillis(500));
                }
            } catch (Exception e) {
                lastError = e;
                System.out.println("Error entering text in " + elementName + ": " + e.getMessage());
                if (attempt < 3) {
                    pause(Duration.ofMillis(500));
                }
            }
        }

        captureDiagnostics("input-failed-" + elementName);
        throw new RuntimeException("Failed to input text in " + elementName, lastError);
    }

    private void logPageSourceForDebug(String context) {
        try {
            String pageSource = driver.getPageSource();
            int maxLength = 2000;
            if (pageSource.length() > maxLength) {
<<<<<<< HEAD
                System.out.println("Page source (truncated) for " + context + ": " +
=======
<<<<<<< HEAD
                System.out.println("Page source (truncated) for " + context + ": " + 
=======
                System.out.println("Page source (truncated) for " + context + ": " +
>>>>>>> 0be48a8 (change login screen code and test case in java file .)
>>>>>>> 169c65d (change login screen code and test case in java file .)
                        pageSource.substring(0, maxLength));
            } else {
                System.out.println("Page source for " + context + ": " + pageSource);
            }
        } catch (Exception e) {
            System.out.println("Could not capture page source: " + e.getMessage());
        }
    }

    private void pause(Duration duration) {
        try {
            Thread.sleep(duration.toMillis());
        } catch (InterruptedException error) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Wait interrupted", error);
        }
    }

    /**
     * Enter email with enhanced error handling
     */
    public void enterEmail(String email) {
        reliableInput(emailFieldLocator, "Email Field", email);
    }

    /**
     * Enter password with enhanced error handling
     */
    public void enterPassword(String password) {
        reliableInput(passwordFieldLocator, "Password Field", password);
        try {
            driver.hideKeyboard();
        } catch (Exception ignored) {
        }
    }

    /**
     * Tap login button with enhanced error handling
     */
    public void tapLogin() {
        reliableClick(loginButtonLocator, "Login Button");
        waitForHomeScreenAppearance();
    }

    /**
     * Complete login flow with retries
     */
    public void login(String email, String password) {
        for (int attempt = 1; attempt <= MAX_LOGIN_RETRIES; attempt++) {
            try {
                System.out.println("Login attempt " + attempt + "/" + MAX_LOGIN_RETRIES);
                enterEmail(email);
                enterPassword(password);
                tapLogin();

                if (!isHomeScreenVisible()) {
                    throw new TimeoutException("Home screen was not loaded after login");
                }
                System.out.println("Login successful on attempt " + attempt);
                return;
            } catch (TimeoutException | NoSuchElementException | StaleElementReferenceException error) {
                System.out.println("Login attempt " + attempt + " failed: " + error.getMessage());
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
            if (!elements.isEmpty()) {
                elements.get(0).clear();
            }
        } catch (Exception ignored) {
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
        reliableClick(rememberMeToggleLocator, "Remember Me Toggle");
    }

    public void togglePasswordVisibility() {
        reliableClick(showPasswordButtonLocator, "Show Password Button");
    }

    private void captureDiagnostics(String name) {
        try {
            Path directory = Path.of("AppiumTests/screenshots");
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
            System.out.println("Diagnostics captured: " + name);
        } catch (Exception error) {
            System.err.println("Unable to capture diagnostics: " + error.getMessage());
        }
    }
}
