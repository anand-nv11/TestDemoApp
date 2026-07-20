import XCTest

final class SwiftUIDemoAllComponentsUITests: XCTestCase {

    private var app: XCUIApplication!

    override func setUpWithError() throws {
        continueAfterFailure = false
        app = XCUIApplication()
    }

    override func tearDownWithError() throws {
        if let app, app.state != .notRunning {
            app.terminate()
        }
        app = nil
    }

    @MainActor
    func testCatalogLaunchesAndOpensButtonDemo() throws {
        app.launchArguments = ["-uiTestOpenButtonDemo"]
        app.launch()

        try login(in: app)

        let buttonTitle = app.staticTexts["Button"]
        XCTAssertTrue(
            buttonTitle.waitForExistence(timeout: 15),
            "Button demo did not appear after login. Current hierarchy:\n\(app.debugDescription)"
        )
    }

    @MainActor
    func testGuideTabShowsBestPractices() throws {
        app.launchArguments = ["-uiTestStartOnGuide"]
        app.launch()

        try login(in: app)

        let guideTab = app.tabBars.buttons["Guide"]
        if guideTab.waitForExistence(timeout: 10) {
            guideTab.tap()
        }

        let projectStructure = app.staticTexts["Project Structure"]
        XCTAssertTrue(
            projectStructure.waitForExistence(timeout: 15),
            "Project Structure was not visible on the Guide screen."
        )

        let bestPractices = app.staticTexts["Best Practices"]
        if !bestPractices.waitForExistence(timeout: 3) {
            app.scrollViews.firstMatch.swipeUp()
        }

        XCTAssertTrue(
            bestPractices.waitForExistence(timeout: 10),
            "Best Practices was not visible after scrolling."
        )
    }

    @MainActor
    func testValidLoginOpensComponentsTab() throws {
        app.launch()
        try login(in: app)

        // A visible tab button is a more stable readiness marker than a SwiftUI
        // TabView container accessibility identifier such as RootTabView.
        let componentsTab = app.tabBars.buttons["Components"]
        XCTAssertTrue(
            componentsTab.waitForExistence(timeout: 15),
            "Components tab was not visible after valid login."
        )
    }

    @MainActor
    func testInvalidEmailKeepsLoginDisabled() throws {
        app.launch()

        let emailField = app.textFields["loginEmailField"]
        let passwordField = app.secureTextFields["loginPasswordField"]
        let loginButton = app.buttons["loginButton"]

        XCTAssertTrue(emailField.waitForExistence(timeout: 15))
        XCTAssertTrue(passwordField.waitForExistence(timeout: 15))
        XCTAssertTrue(loginButton.waitForExistence(timeout: 15))

        emailField.tap()
        emailField.typeText("invalid-email")

        passwordField.tap()
        passwordField.typeText("DemoPass1!")

        XCTAssertFalse(
            loginButton.isEnabled,
            "Login button should remain disabled for an invalid email address."
        )
    }

    @MainActor
    func testLaunchPerformance() throws {
        measure(metrics: [XCTApplicationLaunchMetric()]) {
            XCUIApplication().launch()
        }
    }

    @MainActor
    private func login(in app: XCUIApplication) throws {
        let emailField = app.textFields["loginEmailField"]
        XCTAssertTrue(
            emailField.waitForExistence(timeout: 15),
            "loginEmailField was not found."
        )

        emailField.tap()
        emailField.typeText("demo@example.com")

        let passwordField = app.secureTextFields["loginPasswordField"]
        XCTAssertTrue(
            passwordField.waitForExistence(timeout: 15),
            "loginPasswordField was not found."
        )

        passwordField.tap()
        passwordField.typeText("DemoPass1!")

        let loginButton = app.buttons["loginButton"]
        XCTAssertTrue(
            loginButton.waitForExistence(timeout: 15),
            "loginButton was not found."
        )
        XCTAssertTrue(
            loginButton.isEnabled,
            "loginButton should be enabled for valid credentials."
        )

        loginButton.tap()
    }
}

