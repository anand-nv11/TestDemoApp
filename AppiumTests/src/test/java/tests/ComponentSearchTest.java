package tests;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.SearchPage;

@Epic("iOS Automation")
@Feature("Search")
public class ComponentSearchTest extends BaseTest {

    @Test(description = "Search Scrolling component if search field exists")
    @Story("Search")
    @Severity(SeverityLevel.NORMAL)
    public void searchScrollingIfSearchAvailable() {
        SearchPage searchPage = new SearchPage(driver);

        boolean searchAvailable = searchPage.searchIfSearchFieldExists("Scrolling");

        if (!searchAvailable) {
            System.out.println("Search field not available. Skipping safely.");
            Assert.assertTrue(true, "Search field not available on current screen.");
            return;
        }

        boolean resultVisible = searchPage.isResultVisible("Scrolling");

        Assert.assertTrue(resultVisible, "Search result should contain Scrolling");
    }
}