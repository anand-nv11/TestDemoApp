package tests;

import base.BaseTest;
import io.appium.java_client.ios.IOSDriver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.LoginPage;

import static org.junit.jupiter.api.Assertions.*;

public class LoginScreenTest extends BaseTest {

    @Test
    @DisplayName("Test successful login")
    public void testSuccessfulLogin() {
        IOSDriver driver = getDriver();
        LoginPage loginPage = new LoginPage(driver);
        
        loginPage.login("test@example.com", "password123");
        
        assertTrue(loginPage.isHomeScreenVisible(), "Home screen should be visible after login");
    }

    @Test
    @DisplayName("Test login with invalid credentials")
    public void testLoginWithInvalidCredentials() {
        IOSDriver driver = getDriver();
        LoginPage loginPage = new LoginPage(driver);
        
        loginPage.enterEmail("test@example.com");
        loginPage.enterPassword("wrongpassword");
        loginPage.tapLogin();
        
        assertTrue(loginPage.isLoginScreenVisible(), "Should remain on login screen with invalid credentials");
    }

    @Test
    @DisplayName("Test login screen is visible")
    public void testLoginScreenVisible() {
        IOSDriver driver = getDriver();
        LoginPage loginPage = new LoginPage(driver);
        
        assertTrue(loginPage.isLoginScreenVisible(), "Login screen should be visible");
    }

    @Test
    @DisplayName("Test login button is enabled")
    public void testLoginButtonEnabled() {
        IOSDriver driver = getDriver();
        LoginPage loginPage = new LoginPage(driver);
        
        assertFalse(loginPage.isLoginButtonEnabled(), "Login button should be initially disabled");
    }

    @Test
    @DisplayName("Test remember me toggle")
    public void testRememberMeToggle() {
        IOSDriver driver = getDriver();
        LoginPage loginPage = new LoginPage(driver);
        
        assertDoesNotThrow(() -> loginPage.toggleRememberMe(), "Should be able to toggle Remember Me");
    }

    @Test
    @DisplayName("Test password visibility toggle")
    public void testPasswordVisibilityToggle() {
        IOSDriver driver = getDriver();
        LoginPage loginPage = new LoginPage(driver);
        
        assertDoesNotThrow(() -> loginPage.togglePasswordVisibility(), "Should be able to toggle password visibility");
    }
}
