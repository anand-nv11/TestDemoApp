//
//  SwiftUIDemoAllComponentsUITestsLaunchTests.swift
//  SwiftUIDemoAllComponentsUITests
//
//  Created by NewVision on 20/05/26.
//

import XCTest

final class SwiftUIDemoAllComponentsUITestsLaunchTests: XCTestCase {

    override class var runsForEachTargetApplicationUIConfiguration: Bool {
        true
    }

    override func setUpWithError() throws {
        continueAfterFailure = false
    }

    @MainActor
    func testLaunchShowsLoginScreen() throws {
        let app = XCUIApplication()
        app.launch()

        let emailField = app.textFields["loginEmailField"]
        XCTAssertTrue(
            emailField.waitForExistence(timeout: 15),
            "Login screen did not appear after launch. Current hierarchy:\n\(app.debugDescription)"
        )

        let attachment = XCTAttachment(screenshot: app.screenshot())
        attachment.name = "Launch Screen"
        attachment.lifetime = .keepAlways
        add(attachment)
    }
}
