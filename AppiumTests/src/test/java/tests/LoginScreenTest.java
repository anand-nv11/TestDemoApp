package tests;

import base.BaseTest;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;

@Epic("iOS Automation")
@Feature("Login")
public class LoginScreenTest extends BaseTest {

    private static final String VALID_EMAIL = "demo@example.com";
    private static final String VALID_PASSWORD = "DemoPass1!";

    @Test(description = "Verify successful login")
    @Story("Successful Login")
    @Severity(SeverityLevel.CRITICAL)
    public void testSuccessfulLogin() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(
                VALID_EMAIL,
                VALID_PASSWORD
        );

        Assert.assertTrue(
                loginPage.isHomeScreenVisible(),
                "Home screen should be visible after login"
        );
    }

    @Test(description = "Verify invalid credentials keep user on login screen")
    @Story("Invalid Login")
    public void testLoginWithInvalidCredentials() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.enterEmail("invalid-email");
        loginPage.enterPassword("123");

        Assert.assertFalse(
                loginPage.isLoginButtonEnabled(),
                "Login button should remain disabled for invalid credentials"
        );

        Assert.assertTrue(
                loginPage.isLoginScreenVisible(),
                "User should remain on the login screen"
        );
    }

    @Test(description = "Verify login screen is visible")
    @Story("Login Screen")
    public void testLoginScreenVisible() {
        LoginPage loginPage = new LoginPage(driver);

        Assert.assertTrue(
                loginPage.isLoginScreenVisible(),
                "Login screen should be visible"
        );
    }

    @Test(description = "Verify login button is initially disabled")
    @Story("Validation")
    public void testLoginButtonInitiallyDisabled() {
        LoginPage loginPage = new LoginPage(driver);

        Assert.assertFalse(
                loginPage.isLoginButtonEnabled(),
                "Login button should be initially disabled"
        );
    }

    @Test(description = "Verify Remember Me toggle")
    @Story("Remember Me")
    public void testRememberMeToggle() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.toggleRememberMe();

        Assert.assertTrue(
                loginPage.isLoginScreenVisible(),
                "Login screen should remain visible after toggling Remember Me"
        );
    }

    @Test(description = "Verify password visibility toggle")
    @Story("Password Visibility")
    public void testPasswordVisibilityToggle() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.togglePasswordVisibility();

        Assert.assertTrue(
                loginPage.isLoginScreenVisible(),
                "Login screen should remain visible after toggling password visibility"
        );
    }
}
