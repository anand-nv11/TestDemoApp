package tests;

import base.BaseTest;
import io.appium.java_client.AppiumBy;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.List;

@Epic("iOS Automation")
@Feature("Login Screen")
public class LoginScreenTest extends BaseTest {

    private static final By EMAIL_FIELD = AppiumBy.accessibilityId("loginEmailField");
    private static final By PASSWORD_FIELD = AppiumBy.accessibilityId("loginPasswordField");
    private static final By REMEMBER_ME_TOGGLE = AppiumBy.accessibilityId("loginRememberMeToggle");
    private static final By LOGIN_BUTTON = AppiumBy.accessibilityId("loginButton");
    private static final By LOGIN_ERROR_MESSAGE = AppiumBy.accessibilityId("loginErrorMessage");
    private static final By SHOW_PASSWORD_BUTTON = AppiumBy.accessibilityId("Show password");
    private static final By HIDE_PASSWORD_BUTTON = AppiumBy.accessibilityId("Hide password");

    private WebDriverWait wait;

    @BeforeMethod(alwaysRun = true)
    public void prepareLoginScreen() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL_FIELD));
        wait.until(ExpectedConditions.visibilityOfElementLocated(PASSWORD_FIELD));
    }

    @Test(description = "Verify login screen elements are displayed")
    @Story("Login UI")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyLoginScreenElements() {
        Assert.assertTrue(find(EMAIL_FIELD).isDisplayed(), "Email field is not displayed");
        Assert.assertTrue(find(PASSWORD_FIELD).isDisplayed(), "Password field is not displayed");
        Assert.assertTrue(find(REMEMBER_ME_TOGGLE).isDisplayed(), "Remember-me toggle is not displayed");
        Assert.assertTrue(find(LOGIN_BUTTON).isDisplayed(), "Login button is not displayed");
    }

    @Test(description = "Verify login button is disabled when fields are empty")
    @Story("Login Validation")
    @Severity(SeverityLevel.NORMAL)
    public void verifyLoginButtonDisabledForEmptyForm() {
        clearField(EMAIL_FIELD);
        clearField(PASSWORD_FIELD);

        Assert.assertFalse(
                isEnabled(find(LOGIN_BUTTON)),
                "Login button should be disabled when email and password are empty"
        );
    }

    @DataProvider(name = "invalidEmails")
    public Object[][] invalidEmails() {
        return new Object[][]{
                {"anand"},
                {"anand@"},
                {"@gmail.com"},
                {"anand gmail.com"},
                {"anand@gmail"}
        };
    }

    @Test(
            dataProvider = "invalidEmails",
            description = "Verify login remains disabled for invalid email formats"
    )
    @Story("Email Validation")
    @Severity(SeverityLevel.NORMAL)
    public void verifyInvalidEmailDisablesLogin(String invalidEmail) {
        enterText(EMAIL_FIELD, invalidEmail);
        enterText(PASSWORD_FIELD, "Strong@123");

        Assert.assertFalse(
                isEnabled(find(LOGIN_BUTTON)),
                "Login button should be disabled for invalid email: " + invalidEmail
        );
    }

    @DataProvider(name = "weakPasswords")
    public Object[][] weakPasswords() {
        return new Object[][]{
                {"short1!"},          // fewer than 8 characters
                {"lowercase1!"},      // no uppercase
                {"UPPERCASE1!"},      // no lowercase
                {"NoNumber!"},        // no number
                {"NoSpecial123"},     // no special character
                {"Has Space1!"}       // contains a space
        };
    }

    @Test(
            dataProvider = "weakPasswords",
            description = "Verify login remains disabled for weak passwords"
    )
    @Story("Password Validation")
    @Severity(SeverityLevel.NORMAL)
    public void verifyWeakPasswordDisablesLogin(String weakPassword) {
        enterText(EMAIL_FIELD, "anand@example.com");
        enterText(PASSWORD_FIELD, weakPassword);

        Assert.assertFalse(
                isEnabled(find(LOGIN_BUTTON)),
                "Login button should be disabled for weak password: " + weakPassword
        );
    }

    @Test(description = "Verify login button is enabled for a valid email and strong password")
    @Story("Login Validation")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyLoginButtonEnabledForValidForm() {
        enterText(EMAIL_FIELD, "anand@example.com");
        enterText(PASSWORD_FIELD, "Strong@123");

        Assert.assertTrue(
                isEnabled(find(LOGIN_BUTTON)),
                "Login button should be enabled for a valid form"
        );
    }

    @Test(description = "Verify successful login opens the next screen")
    @Story("Successful Login")
    @Severity(SeverityLevel.BLOCKER)
    public void verifySuccessfulLogin() {
        enterText(EMAIL_FIELD, "anand@example.com");
        enterText(PASSWORD_FIELD, "Strong@123");

        WebElement loginButton = find(LOGIN_BUTTON);
        Assert.assertTrue(isEnabled(loginButton), "Login button should be enabled before tapping");
        loginButton.click();

        wait.until(driver -> driver.findElements(EMAIL_FIELD).isEmpty());

        Assert.assertTrue(
                driver.findElements(EMAIL_FIELD).isEmpty(),
                "Login screen should disappear after successful login"
        );
        Assert.assertNotNull(driver.getPageSource(), "Next screen page source should be available");
    }

    @Test(description = "Verify remember-me toggle can be switched on and off")
    @Story("Remember Me")
    @Severity(SeverityLevel.MINOR)
    public void verifyRememberMeToggle() {
        WebElement toggle = find(REMEMBER_ME_TOGGLE);

        boolean initialState = toggleSelected(toggle);
        toggle.click();
        boolean changedState = toggleSelected(find(REMEMBER_ME_TOGGLE));

        Assert.assertNotEquals(changedState, initialState, "Remember-me toggle state did not change");

        find(REMEMBER_ME_TOGGLE).click();
        boolean restoredState = toggleSelected(find(REMEMBER_ME_TOGGLE));

        Assert.assertEquals(restoredState, initialState, "Remember-me toggle did not return to its original state");
    }

    @Test(description = "Verify password visibility button changes accessibility label")
    @Story("Password Visibility")
    @Severity(SeverityLevel.MINOR)
    public void verifyShowAndHidePassword() {
        enterText(PASSWORD_FIELD, "Strong@123");

        WebElement showButton = wait.until(
                ExpectedConditions.elementToBeClickable(SHOW_PASSWORD_BUTTON)
        );
        showButton.click();

        Assert.assertFalse(
                driver.findElements(HIDE_PASSWORD_BUTTON).isEmpty(),
                "Hide-password button should appear after showing the password"
        );

        find(HIDE_PASSWORD_BUTTON).click();

        Assert.assertFalse(
                driver.findElements(SHOW_PASSWORD_BUTTON).isEmpty(),
                "Show-password button should appear after hiding the password"
        );
    }

    @Test(description = "Verify no login error is displayed for a valid form before submission")
    @Story("Login Error")
    @Severity(SeverityLevel.MINOR)
    public void verifyNoErrorBeforeLoginSubmission() {
        enterText(EMAIL_FIELD, "anand@example.com");
        enterText(PASSWORD_FIELD, "Strong@123");

        List<WebElement> errors = driver.findElements(LOGIN_ERROR_MESSAGE);
        if (!errors.isEmpty()) {
            Assert.assertTrue(
                    errors.get(0).getText() == null || errors.get(0).getText().isBlank(),
                    "Unexpected login error was displayed: " + errors.get(0).getText()
            );
        }
    }

    private WebElement find(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    private void enterText(By locator, String value) {
        WebElement element = find(locator);
        element.click();
        element.clear();
        element.sendKeys(value);
    }

    private void clearField(By locator) {
        WebElement element = find(locator);
        element.click();
        element.clear();
    }

    private boolean isEnabled(WebElement element) {
        String enabled = element.getAttribute("enabled");
        return enabled == null ? element.isEnabled() : Boolean.parseBoolean(enabled);
    }

    private boolean toggleSelected(WebElement toggle) {
        String value = toggle.getAttribute("value");
        if (value != null) {
            return "1".equals(value) || "true".equalsIgnoreCase(value);
        }

        String selected = toggle.getAttribute("selected");
        return "true".equalsIgnoreCase(selected);
    }
}
