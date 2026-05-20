import Testing
@testable import SwiftUIDemoAllComponents

struct SwiftUIDemoAllComponentsTests {
    @Test func profileRequiresNameEmailAndTerms() {
        let viewModel = DemoFormViewModel()

        #expect(viewModel.isProfileValid == false)

        viewModel.profile.name = "Ava"
        viewModel.profile.email = "ava@example.com"
        viewModel.profile.acceptedTerms = true

        #expect(viewModel.isNameValid)
        #expect(viewModel.isEmailValid)
        #expect(viewModel.submitProfile())
    }

    @Test func notesValidationShowsLimitMessage() {
        let viewModel = DemoFormViewModel()

        viewModel.notes = String(repeating: "A", count: 181)

        #expect(viewModel.notesValidationMessage == "Notes must be 180 characters or fewer.")
    }

    @Test func progressDoesNotExceedOneHundredPercent() {
        let viewModel = DemoFormViewModel()
        viewModel.progress = 0.95

        viewModel.advanceProgress()
        viewModel.advanceProgress()

        #expect(viewModel.progress == 1.0)
    }

    @Test func repositoryContainsAllRequestedComponents() {
        #expect(DemoRepository.components.count == 22)
        #expect(DemoRepository.components.map(\.id).contains(.textEditor))
        #expect(DemoRepository.components.map(\.id).contains(.calendarPicker))
    }
}
