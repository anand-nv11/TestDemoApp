import Foundation
import Observation

// MVVM state holder used by the form demos and unit tests.
@Observable
final class DemoFormViewModel {
    var profile = ProfileForm()
    var selectedShippingSpeed: ShippingSpeed = .standard
    var progress = 0.35
    var rating = 4.0
    var selectedDate = Date()
    var selectedTime = Date()
    var selectedDates: Set<DateComponents> = []
    var selectedFont: DemoFontChoice = .body
    var notes = ""
    var isLoading = false
    var selectedTab: DemoTab = .overview
    var tasks: [DemoTask] = [
        DemoTask(title: "Confirm address", isDone: true),
        DemoTask(title: "Choose shipping", isDone: false),
        DemoTask(title: "Review order", isDone: false)
    ]

    var isNameValid: Bool {
        profile.name.trimmingCharacters(in: .whitespacesAndNewlines).count >= 2
    }

    var isEmailValid: Bool {
        let value = profile.email.trimmingCharacters(in: .whitespacesAndNewlines)
        return value.contains("@") && value.contains(".")
    }

    var isProfileValid: Bool {
        isNameValid && isEmailValid && profile.acceptedTerms
    }

    var notesValidationMessage: String? {
        notes.count > 180 ? "Notes must be 180 characters or fewer." : nil
    }

    var canSchedule: Bool {
        selectedDate >= Calendar.current.startOfDay(for: Date())
    }

    func toggleTask(_ task: DemoTask) {
        guard let index = tasks.firstIndex(of: task) else { return }
        tasks[index].isDone.toggle()
    }

    func advanceProgress() {
        progress = min(progress + 0.1, 1.0)
    }

    func resetProgress() {
        progress = 0
    }

    func submitProfile() -> Bool {
        isProfileValid
    }
}
