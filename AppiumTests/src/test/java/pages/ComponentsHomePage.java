package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class ComponentsHomePage {

    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);
    private static final Duration SHORT_TIMEOUT = Duration.ofSeconds(5);

    private final IOSDriver driver;
    private final WebDriverWait wait;

    private final By homeScreenLocator =
            AppiumBy.accessibilityId("homeScreen");

    private final By componentCatalogListLocator =
            AppiumBy.accessibilityId("ComponentCatalogList");

    private final By componentCatalogScreenLocator =
            AppiumBy.accessibilityId("ComponentCatalogScreen");

    private final By rootTabViewLocator =
            AppiumBy.accessibilityId("RootTabView");

    private final By componentsTabLocator =
            AppiumBy.accessibilityId("ComponentsTab");

    private final By guideTabLocator =
            AppiumBy.accessibilityId("GuideTab");

    public ComponentsHomePage(IOSDriver driver) {
        if (driver == null) {
            throw new IllegalArgumentException("IOSDriver cannot be null");
        }

        this.driver = driver;
        this.wait = new WebDriverWait(driver, DEFAULT_TIMEOUT);
    }

    public boolean isAppLoaded() {
        try {
            return wait.until(currentDriver ->
                    isAnyVisible(
                            homeScreenLocator,
                            componentCatalogListLocator,
                            componentCatalogScreenLocator,
                            rootTabViewLocator
                    )
            );
        } catch (TimeoutException error) {
            System.err.println(
                    "App did not load. Expected one of: " +
                            "homeScreen, ComponentCatalogList, " +
                            "ComponentCatalogScreen, RootTabView"
            );
            return false;
        }
    }

    public boolean isComponentsScreenVisible() {
        return isAnyVisible(
                homeScreenLocator,
                componentCatalogListLocator,
                componentCatalogScreenLocator
        );
    }

    public WebElement waitForAccessibilityId(String accessibilityId, int seconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(
                        ExpectedConditions.visibilityOfElementLocated(
                                AppiumBy.accessibilityId(accessibilityId)
                        )
                );
    }

    public WebElement waitForHomeScreen() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        homeScreenLocator
                )
        );
    }

    public WebElement waitForComponentCatalogList() {
        return wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                        componentCatalogListLocator
                )
        );
    }

    public boolean isRootTabViewVisible() {
        return isVisible(rootTabViewLocator);
    }

    public boolean tapComponentsTab() {
        return tapIfPresent(componentsTabLocator);
    }

    public boolean tapGuideTab() {
        return tapIfPresent(guideTabLocator);
    }

    public By componentRowLocator(String componentId) {
        return AppiumBy.accessibilityId(
                "ComponentRow_" + componentId
        );
    }

    public By componentDetailLocator(String componentId) {
        return AppiumBy.accessibilityId(
                "ComponentDetail_" + componentId
        );
    }

    public boolean isComponentRowVisible(String componentId) {
        return isVisible(componentRowLocator(componentId));
    }

    public boolean tapComponentRow(String componentId) {
        By locator = componentRowLocator(componentId);

        for (int attempt = 0; attempt <= 5; attempt++) {
            try {
                WebElement element = new WebDriverWait(
                        driver,
                        SHORT_TIMEOUT
                ).until(
                        ExpectedConditions.elementToBeClickable(locator)
                );

                element.click();
                return true;

            } catch (
                    TimeoutException |
                    NoSuchElementException |
                    StaleElementReferenceException error
            ) {
                if (attempt < 5) {
                    swipeUp();
                    pauseAfterSwipe();
                }
            }
        }

        System.err.println(
                "Component row not found: ComponentRow_" + componentId
        );
        return false;
    }

    public boolean waitForComponentDetail(String componentId) {
        try {
            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            componentDetailLocator(componentId)
                    )
            );
            return true;
        } catch (TimeoutException error) {
            System.err.println(
                    "Component detail not found: ComponentDetail_" + componentId
            );
            return false;
        }
    }

    public boolean tapButtonDemoControl(String accessibilityId) {
        return tapIfPresent(
                AppiumBy.accessibilityId(accessibilityId)
        );
    }

    public boolean tapDefaultButton() {
        return tapButtonDemoControl("DefaultButton");
    }

    public boolean tapProminentButton() {
        return tapButtonDemoControl("ProminentButton");
    }

    public boolean tapBorderedButton() {
        return tapButtonDemoControl("BorderedButton");
    }

    public boolean tapDestructiveButton() {
        return tapButtonDemoControl("DestructiveButton");
    }

    public boolean tapIconButton() {
        return tapButtonDemoControl("IconButton");
    }

    public boolean dismissAlert() {
        return tapIfPresent(
                AppiumBy.accessibilityId("AlertOKButton")
        );
    }

    public String pageSource() {
        return driver.getPageSource();
    }

    public List<WebElement> findByText(String text) {
        String escaped = escapePredicateValue(text);

        return driver.findElements(
                AppiumBy.iOSNsPredicateString(
                        "name == '" + escaped + "' OR " +
                                "label == '" + escaped + "' OR " +
                                "value == '" + escaped + "'"
                )
        );
    }

    public boolean isTextVisible(String text) {
        return findByText(text).stream()
                .anyMatch(this::isDisplayedSafely);
    }

    public boolean tapIfVisible(String text) {
        List<WebElement> elements = findByText(text);

        for (WebElement element : elements) {
            if (isDisplayedSafely(element)) {
                element.click();
                return true;
            }
        }

        return false;
    }

    public boolean tapAccessibilityIdIfVisible(String accessibilityId) {
        return tapIfPresent(
                AppiumBy.accessibilityId(accessibilityId)
        );
    }

    public void swipeUp() {
        driver.executeScript(
                "mobile: swipe",
                Map.of("direction", "up")
        );
    }

    public boolean scrollAndVerifyText(String text, int maxScrolls) {
        for (int attempt = 0; attempt <= maxScrolls; attempt++) {
            if (isTextVisible(text)) {
                return true;
            }

            if (attempt < maxScrolls) {
                swipeUp();
                pauseAfterSwipe();
            }
        }

        return false;
    }

    public boolean scrollAndTapText(String text, int maxScrolls) {
        for (int attempt = 0; attempt <= maxScrolls; attempt++) {
            if (tapIfVisible(text)) {
                return true;
            }

            if (attempt < maxScrolls) {
                swipeUp();
                pauseAfterSwipe();
            }
        }

        return false;
    }

    public int countButtons() {
        return driver.findElements(
                AppiumBy.className("XCUIElementTypeButton")
        ).size();
    }

    public int countCells() {
        return driver.findElements(
                AppiumBy.className("XCUIElementTypeCell")
        ).size();
    }

    public int countTextFields() {
        return driver.findElements(
                AppiumBy.className("XCUIElementTypeTextField")
        ).size();
    }

    public int countSwitches() {
        return driver.findElements(
                AppiumBy.className("XCUIElementTypeSwitch")
        ).size();
    }

    private boolean tapIfPresent(By locator) {
        try {
            WebElement element = wait.until(
                    ExpectedConditions.elementToBeClickable(locator)
            );
            element.click();
            return true;
        } catch (TimeoutException error) {
            return false;
        }
    }

    private boolean isAnyVisible(By... locators) {
        for (By locator : locators) {
            if (isVisible(locator)) {
                return true;
            }
        }

        return false;
    }

    private boolean isVisible(By locator) {
        try {
            List<WebElement> elements = driver.findElements(locator);

            return elements.stream()
                    .anyMatch(this::isDisplayedSafely);

        } catch (Exception error) {
            return false;
        }
    }

    private boolean isDisplayedSafely(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (
                NoSuchElementException |
                StaleElementReferenceException error
        ) {
            return false;
        }
    }

    private String escapePredicateValue(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("'", "\\'");
    }

    private void pauseAfterSwipe() {
        try {
            Thread.sleep(700);
        } catch (InterruptedException error) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException(
                    "Swipe wait interrupted",
                    error
            );
        }
    }
}
