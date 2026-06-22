package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class SearchPage {

    private final IOSDriver driver;

    public SearchPage(IOSDriver driver) {
        this.driver = driver;
    }

    public boolean searchIfSearchFieldExists(String keyword) {
        List<WebElement> searchFields =
                driver.findElements(AppiumBy.iOSClassChain("**/XCUIElementTypeSearchField"));

        if (searchFields.isEmpty()) {
            System.out.println("Search field not found.");
            return false;
        }

        WebElement search = searchFields.get(0);
        search.click();
        search.sendKeys(keyword);

        return true;
    }

    public boolean isResultVisible(String keyword) {
        return !driver.findElements(AppiumBy.iOSNsPredicateString(
                "name CONTAINS '" + keyword + "' OR label CONTAINS '" + keyword + "' OR value CONTAINS '" + keyword + "'"
        )).isEmpty();
    }
}