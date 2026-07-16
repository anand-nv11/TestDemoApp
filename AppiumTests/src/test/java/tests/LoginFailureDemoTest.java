package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;
import pages.LoginPage;

public class LoginFailureDemoTest extends BaseTest {

    @Test(description = "Intentional login failure for report testing")
    public void intentionalLoginFailure() {

        boolean runFailureDemo = Boolean.parseBoolean(
                System.getProperty("runFailureDemo", "false")
        );

        if (!runFailureDemo) {
            throw new SkipException(
                    "Failure demo is disabled. Run with -DrunFailureDemo=true"
            );
        }

        LoginPage loginPage = new LoginPage(driver);

        loginPage.enterEmail("invalid-email");
        loginPage.enterPassword("123");

        String actualError = loginPage.getErrorMessage();

        Assert.assertEquals(
                actualError,
                "Login Successful",
                "Intentional failure for GitHub Actions report demo"
        );
    }
}

