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
import java.util.List;

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
            AppiumBy.accessibilityId("homeScreen");

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
            // Try to get clickable element directly
            return wait.until(
                    ExpectedConditions.elementToBeClickable(
                            locator
                    )
            );
        } catch (StaleElementReferenceException e) {
            // If element becomes stale, retry with fresh lookup
            System.err.println(
                    "Element became stale, retrying clickable wait..."
            );
            return wait.until(
                    ExpectedConditions.elementToBeClickable(
                            locator
                    )
            );
        }
    }

    public void enterEmail(String email) {

        System.out.println(
                "Waiting for login email field..."
        );

        WebElement emailField =
                waitForVisible(emailFieldLocator);

        emailField.clear();
        emailField.sendKeys(email);

        System.out.println(
                "Email entered successfully."
        );
    }

    public void enterPassword(String password) {

        System.out.println(
                "Waiting for login password field..."
        );

        WebElement passwordField =
                waitForVisible(passwordFieldLocator);

        passwordField.clear();
        passwordField.sendKeys(password);

        System.out.println(
                "Password entered successfully."
        );
    }

    public void tapLogin() {

        System.out.println(
                "Waiting for login button..."
        );

        WebElement loginButton =
                waitForClickable(loginButtonLocator);

        loginButton.click();

        System.out.println(
                "Login button clicked successfully."
        );

        // Wait for navigation to complete before returning
        waitForHomeScreenAppearance();
    }

    private void waitForHomeScreenAppearance() {

        System.out.println(
                "Waiting for home screen to appear..."
        );

        try {
            wait.until(
                    ExpectedConditions.presenceOfElementLocated(
                            homeScreenLocator
                    )
            );
            System.out.println(
                    "Home screen appeared successfully."
            );
        } catch (TimeoutException e) {
            System.err.println(
                    "Home screen did not appear within timeout."
            );
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

                System.out.println(
                        "Login attempt: "
                                + attempt
                                + "/"
                                + MAX_LOGIN_RETRIES
                );

                enterEmail(email);
                enterPassword(password);
                tapLogin();

                System.out.println(
                        "Login action completed."
                );

                return;

            } catch (
                    TimeoutException |
                    NoSuchElementException |
                    StaleElementReferenceException exception
            ) {

                System.err.println(
                        "Login attempt "
                                + attempt
                                + " failed: "
                                + exception.getMessage()
                );

                if (attempt == MAX_LOGIN_RETRIES) {

                    System.err.println(
                            "Maximum login retries reached."
                    );

                    throw exception;
                }

                waitBeforeRetry();
            }
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

        System.out.println(
                "Waiting for login error message..."
        );

        WebElement errorMessage =
                waitForVisible(loginErrorLocator);

        String message = errorMessage.getText();

        System.out.println(
                "Login error message: " + message
        );

        return message;
    }

    public boolean isLoginButtonEnabled() {

        try {

            WebElement loginButton =
                    waitForPresent(loginButtonLocator);

            return loginButton.isEnabled();

        } catch (TimeoutException exception) {

            System.err.println(
                    "Login button was not found."
            );

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

            System.err.println(
                    "Home screen was not visible within timeout."
            );
            return false;
        }
    }

    public void toggleRememberMe() {

        System.out.println(
                "Waiting for Remember Me toggle..."
        );

        WebElement rememberMeToggle =
                waitForClickable(
                        rememberMeToggleLocator
                );

        rememberMeToggle.click();

        System.out.println(
                "Remember Me toggle clicked."
        );
    }

    public void togglePasswordVisibility() {

        System.out.println(
                "Waiting for password visibility button..."
        );

        WebElement showPasswordButton =
                waitForClickable(
                        showPasswordButtonLocator
                );

        showPasswordButton.click();

        System.out.println(
                "Password visibility button clicked."
        );
    }
}
