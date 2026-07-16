import Foundation
import Observation
import SwiftUI

struct DemoTask: Identifiable, Hashable {
    let id = UUID()
    var title: String
    var isDone: Bool
}

struct ProfileForm: Equatable {
    var name = ""
    var email = ""
    var bio = ""
    var newsletterEnabled = true
    var acceptedTerms = false
}

enum ShippingSpeed: String, CaseIterable, Identifiable {
    case standard = "Standard"
    case express = "Express"
    case overnight = "Overnight"

    var id: String { rawValue }

    var detail: String {
        switch self {
        case .standard: "3-5 business days"
        case .express: "2 business days"
        case .overnight: "Next business day"
        }
    }
}

enum DemoFontChoice: String, CaseIterable, Identifiable {
    case body = "Body"
    case rounded = "Rounded"
    case serif = "Serif"
    case monospaced = "Monospaced"

    var id: String { rawValue }

    var font: Font {
        switch self {
        case .body: .body
        case .rounded: .system(.body, design: .rounded)
        case .serif: .system(.body, design: .serif)
        case .monospaced: .system(.body, design: .monospaced)
        }
    }
}

enum DemoTab: String, CaseIterable, Identifiable {
    case overview = "Overview"
    case activity = "Activity"
    case settings = "Settings"

    var id: String { rawValue }
}

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
