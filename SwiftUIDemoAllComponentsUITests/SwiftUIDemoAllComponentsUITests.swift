import XCTest

final class SwiftUIDemoAllComponentsUITests: XCTestCase {
    override func setUpWithError() throws {
        continueAfterFailure = false
    }

    @MainActor
    func testCatalogLaunchesAndOpensButtonDemo() throws {
        let app = XCUIApplication()
        app.launchArguments.append("-uiTestOpenButtonDemo")
        app.launch()
        loginAndOpenComponentDemo(in: app, waitsForComponentsTab: false)

        XCTAssertTrue(app.staticTexts["Button"].waitForExistence(timeout: 5))
    }

    @MainActor
    func testGuideTabShowsBestPractices() throws {
        let app = XCUIApplication()
        app.launchArguments.append("-uiTestStartOnGuide")
        app.launch()
        loginAndOpenComponentDemo(in: app)

        XCTAssertTrue(app.staticTexts["Project Structure"].waitForExistence(timeout: 5))
        let bestPractices = app.staticTexts["Best Practices"]
        if !bestPractices.exists {
            app.scrollViews.firstMatch.swipeUp()
        }
        XCTAssertTrue(bestPractices.waitForExistence(timeout: 5))
    }

    @MainActor
    func testLaunchPerformance() throws {
        measure(metrics: [XCTApplicationLaunchMetric()]) {
            XCUIApplication().launch()
        }
    }

    @MainActor
    private func loginAndOpenComponentDemo(in app: XCUIApplication, waitsForComponentsTab: Bool = true) {
        app.textFields["loginEmailField"].tap()
        app.textFields["loginEmailField"].typeText("demo@example.com")

        app.secureTextFields["loginPasswordField"].tap()
        app.secureTextFields["loginPasswordField"].typeText("DemoPass1!")

        app.buttons["loginButton"].tap()

        if waitsForComponentsTab {
            XCTAssertTrue(app.tabBars.buttons["Components"].waitForExistence(timeout: 5))
        }
    }

}
