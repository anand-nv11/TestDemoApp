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
    func testLaunch() throws {
        let app = XCUIApplication()
        app.launch()

        XCTAssertTrue(app.textFields["loginEmailField"].waitForExistence(timeout: 5))

        let attachment = XCTAttachment(screenshot: app.screenshot())
        attachment.name = "Launch Screen"
        attachment.lifetime = .keepAlways
        add(attachment)
    }
}
