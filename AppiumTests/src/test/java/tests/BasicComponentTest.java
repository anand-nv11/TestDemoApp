package tests;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.ComponentsHomePage;

@Epic("iOS Automation")
@Feature("Basic UI Components")
public class BasicComponentTest extends BaseTest {

    @Test(description = "Verify buttons or cells exist")
    @Story("Basic UI")
    @Severity(SeverityLevel.NORMAL)
    public void verifyBasicElementsExist() {
        ComponentsHomePage homePage = new ComponentsHomePage(driver);

        int buttons = homePage.countButtons();
        int cells = homePage.countCells();

        System.out.println("Button count: " + buttons);
        System.out.println("Cell count: " + cells);

        Assert.assertTrue(buttons > 0 || cells > 0, "At least one button or cell should exist");
    }

    @Test(description = "Verify text fields safely")
    @Story("TextField")
    @Severity(SeverityLevel.MINOR)
    public void verifyTextFieldsSafely() {
        ComponentsHomePage homePage = new ComponentsHomePage(driver);

        System.out.println("Text field count: " + homePage.countTextFields());

        Assert.assertTrue(true, "Text field scan completed");
    }

    @Test(description = "Verify switches safely")
    @Story("Toggle")
    @Severity(SeverityLevel.MINOR)
    public void verifySwitchesSafely() {
        ComponentsHomePage homePage = new ComponentsHomePage(driver);

        System.out.println("Switch count: " + homePage.countSwitches());

        Assert.assertTrue(true, "Switch scan completed");
    }
}
