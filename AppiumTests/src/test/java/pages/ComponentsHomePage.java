package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class ComponentsHomePage {

    private static final Duration WAIT_TIMEOUT = Duration.ofSeconds(20);

    private final IOSDriver driver;
    private final WebDriverWait wait;

    public ComponentsHomePage(IOSDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("IOSDriver cannot be null");
        }
        this.driver = driver;
        this.wait = new WebDriverWait(driver, WAIT_TIMEOUT);
    }

    private By componentsTabLocator() {
        return AppiumBy.iOSNsPredicateString(
                "type == 'XCUIElementTypeButton' AND " +
                        "(name == 'Components' OR label == 'Components')"
        );
    }

    public boolean isAppLoaded() {
        try {
            return wait.until(currentDriver -> {
                boolean loginVisible = !currentDriver.findElements(
                        AppiumBy.accessibilityId("loginEmailField")
                ).isEmpty();

                boolean componentsVisible = !currentDriver.findElements(
                        componentsTabLocator()
                ).isEmpty();

                return loginVisible || componentsVisible;
            });
        } catch (TimeoutException error) {
            System.err.println("Neither login screen nor Components tab became visible.");
            return false;
        }
    }

    public boolean isComponentsScreenVisible() {
        return !driver.findElements(componentsTabLocator()).isEmpty();
    }

    public String pageSource() {
        return driver.getPageSource();
    }

    public List<WebElement> findByText(String text) {
        String escaped = text.replace("'", "\\'");
        return driver.findElements(AppiumBy.iOSNsPredicateString(
                "name == '" + escaped + "' OR " +
                        "label == '" + escaped + "' OR " +
                        "value == '" + escaped + "'"
        ));
    }

    public boolean isTextVisible(String text) {
        return !findByText(text).isEmpty();
    }

    public boolean tapIfVisible(String text) {
        List<WebElement> elements = findByText(text);
        if (elements.isEmpty()) return false;
        elements.get(0).click();
        return true;
    }

    public boolean tapAccessibilityIdIfVisible(String accessibilityId) {
        List<WebElement> elements = driver.findElements(
                AppiumBy.accessibilityId(accessibilityId)
        );
        if (elements.isEmpty()) return false;
        elements.get(0).click();
        return true;
    }

    public void swipeUp() {
        driver.executeScript("mobile: swipe", Map.of("direction", "up"));
    }

    public boolean scrollAndVerifyText(String text, int maxScrolls) {
        for (int attempt = 0; attempt <= maxScrolls; attempt++) {
            if (isTextVisible(text)) return true;
            if (attempt < maxScrolls) {
                swipeUp();
                pauseAfterSwipe();
            }
        }
        return false;
    }

    public boolean scrollAndTapText(String text, int maxScrolls) {
        for (int attempt = 0; attempt <= maxScrolls; attempt++) {
            if (tapIfVisible(text)) return true;
            if (attempt < maxScrolls) {
                swipeUp();
                pauseAfterSwipe();
            }
        }
        return false;
    }

    public int countButtons() {
        return driver.findElements(AppiumBy.className("XCUIElementTypeButton")).size();
    }

    public int countCells() {
        return driver.findElements(AppiumBy.className("XCUIElementTypeCell")).size();
    }

    public int countTextFields() {
        return driver.findElements(AppiumBy.className("XCUIElementTypeTextField")).size();
    }

    public int countSwitches() {
        return driver.findElements(AppiumBy.className("XCUIElementTypeSwitch")).size();
    }

    private void pauseAfterSwipe() {
        try {
            Thread.sleep(700);
        } catch (InterruptedException error) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Swipe wait interrupted", error);
        }
    }
}
