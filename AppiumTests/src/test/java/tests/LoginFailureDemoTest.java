package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;

public class LoginFailureDemoTest extends BaseTest {

    @Test(
            groups = "failure-demo",
            description = "Intentional failure for report verification"
    )
    public void intentionalLoginFailure() {
        LoginPage loginPage = new LoginPage(driver);

        loginPage.enterEmail("invalid-email");
        loginPage.enterPassword("123");
        loginPage.tapLogin();

        String actualError = loginPage.getEmailError();

        // Intentionally incorrect expected message
        Assert.assertEquals(
                actualError,
                "Login successful",
                "Intentional failure: invalid credentials must not log in"
        );
    }
}
