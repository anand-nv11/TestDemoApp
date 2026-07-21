package tests;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.ComponentsHomePage;

@Epic("iOS Automation")
@Feature("App Launch")
public class AppLaunchTest extends BaseTest {

    @Test(description = "Verify SwiftUI app launches successfully")
    @Story("Launch")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyAppLaunch() {
        ComponentsHomePage homePage = new ComponentsHomePage(driver);

        Assert.assertNotNull(driver, "Driver should not be null");
        Assert.assertTrue(
                homePage.isAppLoaded(),
                "Application did not finish loading."
        );

        System.out.println(driver.getPageSource());

        System.out.println("App launched successfully.");
    }
}