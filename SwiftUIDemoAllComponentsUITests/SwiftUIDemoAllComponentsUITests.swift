import XCTest

final class SwiftUIDemoAllComponentsUITests: XCTestCase {
    override func setUpWithError() throws {
        continueAfterFailure = false
    }

    @MainActor
    func testCatalogLaunchesAndOpensButtonDemo() throws {
        let app = XCUIApplication()
        app.launch()

        XCTAssertTrue(app.navigationBars["SwiftUI Components"].waitForExistence(timeout: 5))

        let buttonRow = app.buttons["Button, Actions, roles, custom styles, loading state, and animation."]
        if buttonRow.exists {
            buttonRow.tap()
        } else {
            app.collectionViews.buttons.matching(identifier: "ComponentRow_button").firstMatch.tap()
        }

        XCTAssertTrue(app.navigationBars["Button"].waitForExistence(timeout: 5))
        XCTAssertTrue(app.staticTexts["Button"].exists)
    }

    @MainActor
    func testGuideTabShowsBestPractices() throws {
        let app = XCUIApplication()
        app.launch()

        app.tabBars.buttons["Guide"].tap()

        XCTAssertTrue(app.navigationBars["SwiftUI Guide"].waitForExistence(timeout: 5))
        XCTAssertTrue(app.staticTexts["Best Practices"].exists)
    }

    @MainActor
    func testLaunchPerformance() throws {
        measure(metrics: [XCTApplicationLaunchMetric()]) {
            XCUIApplication().launch()
        }
    }
}
