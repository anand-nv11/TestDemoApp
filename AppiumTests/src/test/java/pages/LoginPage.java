package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class LoginPage {

    private final IOSDriver driver;
    private final WebDriverWait wait;

    public LoginPage(IOSDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(15)
        );
    }

    public void enterEmail(String email) {
        WebElement emailField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        AppiumBy.accessibilityId("loginEmailField")
                )
        );

        emailField.clear();
        emailField.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement passwordField = wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        AppiumBy.accessibilityId("loginPasswordField")
                )
        );

        passwordField.clear();
        passwordField.sendKeys(password);
    }

    public void tapLogin() {
        wait.until(
                ExpectedConditions.elementToBeClickable(
                        AppiumBy.accessibilityId("loginButton")
                )
        ).click();
    }

    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        tapLogin();
    }

    public String getErrorMessage() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        AppiumBy.accessibilityId("loginErrorMessage")
                )
        ).getText();
    }

    public boolean isLoginButtonEnabled() {
        return wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        AppiumBy.accessibilityId("loginButton")
                )
        ).isEnabled();
    }

    public boolean isLoginScreenVisible() {
        List<WebElement> emailFields = driver.findElements(
                AppiumBy.accessibilityId("loginEmailField")
        );

        return !emailFields.isEmpty();
    }

    public boolean isHomeScreenVisible() {
        List<WebElement> homeScreens = driver.findElements(
                AppiumBy.accessibilityId("homeScreen")
        );

        return !homeScreens.isEmpty();
    }

    public void toggleRememberMe() {
        wait.until(
                ExpectedConditions.elementToBeClickable(
                        AppiumBy.accessibilityId("rememberMeToggle")
                )
        ).click();
    }

    public void togglePasswordVisibility() {
        wait.until(
                ExpectedConditions.elementToBeClickable(
                        AppiumBy.accessibilityId("showPasswordButton")
                )
        ).click();
    }
}
