package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;

public class ComponentsHomePage {

    private final IOSDriver driver;

    public ComponentsHomePage(IOSDriver driver) {
        this.driver = driver;
    }

    public boolean isAppLoaded() {
        return driver != null && driver.getPageSource() != null && !driver.getPageSource().isEmpty();
    }

    public String pageSource() {
        return driver.getPageSource();
    }

    public List<WebElement> findByText(String text) {
        return driver.findElements(AppiumBy.iOSNsPredicateString(
                "name == '" + text + "' OR label == '" + text + "' OR value == '" + text + "'"
        ));
    }

    public boolean isTextVisible(String text) {
        return !findByText(text).isEmpty();
    }

    public boolean tapIfVisible(String text) {
        List<WebElement> elements = findByText(text);
        if (elements.isEmpty()) {
            System.out.println(text + " not found.");
            return false;
        }

        elements.get(0).click();
        return true;
    }

    public boolean tapAccessibilityIdIfVisible(String accessibilityId) {
        List<WebElement> elements = driver.findElements(AppiumBy.accessibilityId(accessibilityId));
        if (elements.isEmpty()) {
            System.out.println(accessibilityId + " not found.");
            return false;
        }

        elements.get(0).click();
        return true;
    }

    public void swipeUp() {
        driver.executeScript("mobile: swipe", Map.of("direction", "up"));
    }

    public boolean scrollAndVerifyText(String text, int maxScrolls) {
        for (int i = 0; i <= maxScrolls; i++) {
            if (isTextVisible(text)) {
                return true;
            }

            swipeUp();
            sleep();
        }

        return false;
    }

    public boolean scrollAndTapText(String text, int maxScrolls) {
        for (int i = 0; i <= maxScrolls; i++) {
            if (tapIfVisible(text)) {
                return true;
            }

            swipeUp();
            sleep();
        }

        return false;
    }

    public int countButtons() {
        return driver.findElements(AppiumBy.iOSClassChain("**/XCUIElementTypeButton")).size();
    }

    public int countCells() {
        return driver.findElements(AppiumBy.iOSClassChain("**/XCUIElementTypeCell")).size();
    }

    public int countTextFields() {
        return driver.findElements(AppiumBy.iOSClassChain("**/XCUIElementTypeTextField")).size();
    }

    public int countSwitches() {
        return driver.findElements(AppiumBy.iOSClassChain("**/XCUIElementTypeSwitch")).size();
    }

    private void sleep() {
        try {
            Thread.sleep(800);
        } catch (InterruptedException ignored) {
        }
    }
}