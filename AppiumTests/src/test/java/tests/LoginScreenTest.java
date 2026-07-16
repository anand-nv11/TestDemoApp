package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;

public class LoginScreenTest extends BaseTest {

    @Test(description = "Verify login screen is displayed")
    public void verifyLoginScreenIsVisible() {
        LoginPage loginPage = new LoginPage(driver);

        Assert.assertTrue(
                loginPage.isLoginScreenVisible(),
                "Login screen is not visible"
        );
    }

    @Test(description = "Verify valid login")
    public void verifyValidLogin() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.login(
                "anand@example.com",
                "Password@123"
        );

        Assert.assertTrue(
                loginPage.isHomeScreenVisible(),
                "Home screen was not displayed after valid login"
        );
    }

    @Test(description = "Verify invalid email validation")
    public void verifyInvalidEmailValidation() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.enterEmail("invalid-email");
        loginPage.enterPassword("Password@123");

        Assert.assertFalse(
                loginPage.isLoginButtonEnabled(),
                "Login button should remain disabled for invalid email"
        );
    }

    @Test(description = "Verify weak password validation")
    public void verifyWeakPasswordValidation() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.enterEmail("anand@example.com");
        loginPage.enterPassword("123");

        Assert.assertFalse(
                loginPage.isLoginButtonEnabled(),
                "Login button should remain disabled for weak password"
        );
    }

    @Test(description = "Verify valid credentials enable login button")
    public void verifyValidCredentialsEnableLoginButton() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.enterEmail("anand@example.com");
        loginPage.enterPassword("Password@123");

        Assert.assertTrue(
                loginPage.isLoginButtonEnabled(),
                "Login button should be enabled for valid credentials"
        );
    }
}