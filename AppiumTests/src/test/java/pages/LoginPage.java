package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginPage {

    private static final Duration WAIT_TIMEOUT =
            Duration.ofSeconds(30);

    private static final Duration RETRY_DELAY =
            Duration.ofSeconds(2);

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
            AppiumBy.accessibilityId("RootTabView");

    private final By rememberMeToggleLocator =
            AppiumBy.accessibilityId("rememberMeToggle");

    private final By showPasswordButtonLocator =
            AppiumBy.accessibilityId("showPasswordButton");

    public LoginPage(IOSDriver driver) {

        if (driver == null) {
            throw new IllegalArgumentException(
                    "IOSDriver cannot be null"
            );
        }

        this.driver = driver;
        this.wait = new WebDriverWait(
                driver,
                WAIT_TIMEOUT
        );
    }

    private WebElement waitForPresent(By locator) {

        return wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        locator
                )
        );
    }

    private WebElement waitForVisible(By locator) {

        WebElement element = waitForPresent(locator);

        return wait.until(
                ExpectedConditions.visibilityOf(element)
        );
    }

    private WebElement waitForClickable(By locator) {

        try {
            // Custom wait: presence + visible + enabled, ignoring transient/stale errors
            return wait.until(d -> {
                try {
                    WebElement el = d.findElement(locator);
                    try {
                        return (el.isDisplayed() && el.isEnabled()) ? el : null;
                    } catch (StaleElementReferenceException | NoSuchElementException se) {
                        // Element became stale between find and checks: retry
                        return null;
                    }
                } catch (NoSuchElementException e) {
                    return null;
                } catch (Exception e) {
                    // Any transient exception -> retry
                    return null;
                }
            });
        } catch (TimeoutException e) {
            // Log the failure with element inspection
            System.err.println("Element timeout waiting for clickable: " + locator);

            // Try to inspect what elements are actually available
            try {
                List<WebElement> allButtons = driver.findElements(
                        AppiumBy.className("XCUIElementTypeButton")
                );
                System.err.println("Found " + allButtons.size() + " buttons on screen");
                for (WebElement button : allButtons) {
                    // Use label and name which are supported by WDA.
                    String label = "";
                    String name = "";
                    try { label = button.getAttribute("label"); } catch (Exception ex) { /* ignore */ }
                    try { name = button.getAttribute("name"); } catch (Exception ex) { /* ignore */ }
                    System.err.println("  - Button: " + label + " | name: " + name);
                }
            } catch (Exception inspectError) {
                System.err.println("Could not inspect elements: " + inspectError.getMessage());
            }

            throw e;
        }
    }

    private void safeClick(By locator) {
        Exception lastEx = null;
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                WebElement el = waitForClickable(locator);
                el.click();
                return;
            } catch (Exception e) {
                lastEx = e;
                System.err.println("Click attempt " + attempt + " failed for " + locator + ": " + e.getMessage());

                // Try to hide the keyboard, give it a short pause, then retry.
                try {
                    driver.hideKeyboard();
                } catch (Exception hideEx) {
                    // ignore if no keyboard present or hide fails
                }
                try {
                    Thread.sleep(300);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }

                // On last attempt, fall back to coordinate tap
                if (attempt == 3) {
                    try {
                        WebElement el = driver.findElement(locator);
                        int centerX = el.getLocation().getX() + el.getSize().getWidth() / 2;
                        int centerY = el.getLocation().getY() + el.getSize().getHeight() / 2;
                        Map<String, Object> args = new HashMap<>();
                        args.put("x", centerX);
                        args.put("y", centerY);
                        driver.executeScript("mobile: tap", args);
                        return;
                    } catch (Exception fallbackEx) {
                        System.err.println("fallback tap failed: " + fallbackEx.getMessage());
                    }
                }
            }
        }
        throw new RuntimeException("Unable to click element: " + locator, lastEx);
    }

    public void enterEmail(String email) {

        System.out.println("Waiting for login email field...");

        WebElement emailField = waitForVisible(emailFieldLocator);

        emailField.clear();
        emailField.sendKeys(email);

        System.out.println("Email entered successfully.");
    }

    public void enterPassword(String password) {

        System.out.println("Waiting for login password field...");

        WebElement passwordField = waitForVisible(passwordFieldLocator);

        passwordField.clear();
        passwordField.sendKeys(password);

        // try to dismiss keyboard to reveal possible controls behind it
        try {
            driver.hideKeyboard();
        } catch (Exception ignored) {
        }

        System.out.println("Password entered successfully.");
    }

    public void tapLogin() {

        System.out.println("Waiting for login button...");

        safeClick(loginButtonLocator);

        System.out.println("Login button clicked successfully.");

        // Wait for navigation to complete before returning
        waitForHomeScreenAppearance();
    }

    private void waitForHomeScreenAppearance() {

        System.out.println("Waiting for home screen to appear (RootTabView)...");

        try {
            wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            homeScreenLocator
                    )
            );
            System.out.println("Home screen appeared successfully.");
        } catch (TimeoutException e) {
            System.err.println("Home screen did not appear within timeout.");
            // Don't rethrow here; let the test assertion handle it
        }
    }

    public void login(
            String email,
            String password
    ) {

        for (
                int attempt = 1;
                attempt <= MAX_LOGIN_RETRIES;
                attempt++
        ) {

            try {

                System.out.println("Login attempt: " + attempt + "/" + MAX_LOGIN_RETRIES);

                enterEmail(email);
                enterPassword(password);
                tapLogin();

                System.out.println("Login action completed.");

                return;

            } catch (
                    TimeoutException |
                    NoSuchElementException |
                    StaleElementReferenceException exception
            ) {

                System.err.println("Login attempt " + attempt + " failed: " + exception.getMessage());

                if (attempt == MAX_LOGIN_RETRIES) {

                    System.err.println("Maximum login retries reached.");

                    throw exception;
                }

                // Reset fields before retry
                resetLoginFields();
                waitBeforeRetry();
            }
        }
    }

    /**
     * Reset login fields for retry attempts
     */
    private void resetLoginFields() {
        try {
            System.out.println("Resetting login fields for retry...");

            // Try to clear email field
            try {
                List<WebElement> emailFields = driver.findElements(
                        emailFieldLocator
                );
                if (!emailFields.isEmpty()) {
                    emailFields.get(0).clear();
                    System.out.println("Email field cleared.");
                }
            } catch (Exception e) {
                System.err.println("Could not clear email field: " + e.getMessage());
            }

            // Try to clear password field
            try {
                List<WebElement> passwordFields = driver.findElements(
                        passwordFieldLocator
                );
                if (!passwordFields.isEmpty()) {
                    passwordFields.get(0).clear();
                    System.out.println("Password field cleared.");
                }
            } catch (Exception e) {
                System.err.println("Could not clear password field: " + e.getMessage());
            }

        } catch (Exception e) {
            System.err.println("Error resetting login fields: " + e.getMessage());
        }
    }

    private void waitBeforeRetry() {

        try {

            Thread.sleep(
                    RETRY_DELAY.toMillis()
            );

        } catch (InterruptedException exception) {

            Thread.currentThread().interrupt();

            throw new IllegalStateException(
                    "Login retry was interrupted",
                    exception
            );
        }
    }

    public String getErrorMessage() {

        System.out.println("Waiting for login error message...");

        WebElement errorMessage =
                waitForVisible(loginErrorLocator);

        String message = errorMessage.getText();

        System.out.println("Login error message: " + message);

        return message;
    }

    public boolean isLoginButtonEnabled() {

        try {

            WebElement loginButton =
                    waitForPresent(loginButtonLocator);

            return loginButton.isEnabled();

        } catch (TimeoutException exception) {

            System.err.println("Login button was not found.");

            return false;
        }
    }

    public boolean isLoginScreenVisible() {

        List<WebElement> emailFields =
                driver.findElements(
                        emailFieldLocator
                );

        return !emailFields.isEmpty()
                && emailFields.get(0).isDisplayed();
    }

    public boolean isHomeScreenVisible() {

        try {

            WebElement homeScreen =
                    waitForVisible(homeScreenLocator);

            return homeScreen.isDisplayed();

        } catch (TimeoutException exception) {

            System.err.println("Home screen was not visible within timeout.");
            return false;
        }
    }

    public void toggleRememberMe() {

        System.out.println("Waiting for Remember Me toggle...");

        safeClick(rememberMeToggleLocator);

        System.out.println("Remember Me toggle clicked.");
    }

    public void togglePasswordVisibility() {

        System.out.println("Waiting for password visibility button...");

        safeClick(showPasswordButtonLocator);

        System.out.println("Password visibility button clicked.");
    }
}
