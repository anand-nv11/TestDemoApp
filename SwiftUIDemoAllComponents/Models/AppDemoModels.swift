import Foundation
import SwiftUI

// A simple model used to drive the NavigationStack list.
struct ComponentDemo: Identifiable, Hashable {
    let id: ComponentKind
    let title: String
    let systemImage: String
    let summary: String
}

enum ComponentKind: String, CaseIterable, Identifiable, Hashable {
    case box
    case button
    case calendarPicker
    case checkbox
    case clockPicker
    case colorWell
    case datePicker
    case fontPicker
    case helpButton
    case image
    case label
    case progress
    case popups
    case radioButton
    case divider
    case slider
    case scrollView
    case toggle
    case tabView
    case textField
    case textEditor
    case timePicker

    var id: String { rawValue }
}

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

struct DemoRepository {
    static let components: [ComponentDemo] = [
        .init(id: .box, title: "Box", systemImage: "shippingbox", summary: "Containers, cards, backgrounds, spacing, and responsive layout."),
        .init(id: .button, title: "Button", systemImage: "hand.tap", summary: "Actions, roles, custom styles, loading state, and animation."),
        .init(id: .calendarPicker, title: "CalendarPicker", systemImage: "calendar", summary: "Single and multi-date calendar selection for scheduling flows."),
        .init(id: .checkbox, title: "Checkbox", systemImage: "checkmark.square", summary: "A reusable checkbox built from Button and Toggle-like state."),
        .init(id: .clockPicker, title: "ClockPicker", systemImage: "clock", summary: "Hour and minute selection with appointment validation."),
        .init(id: .colorWell, title: "ColorWell", systemImage: "paintpalette", summary: "SwiftUI ColorPicker examples for theme customization."),
        .init(id: .datePicker, title: "DatePicker", systemImage: "calendar.badge.clock", summary: "Date ranges, graphical style, and booking validation."),
        .init(id: .fontPicker, title: "FontPicker", systemImage: "textformat", summary: "Reusable font choice picker for previewing readable typography."),
        .init(id: .helpButton, title: "HelpButton", systemImage: "questionmark.circle", summary: "Contextual help with popovers, labels, and accessibility."),
        .init(id: .image, title: "Image", systemImage: "photo", summary: "System and asset images with resizing, clipping, and labels."),
        .init(id: .label, title: "Label", systemImage: "tag", summary: "Text plus icon labels, styles, and status rows."),
        .init(id: .progress, title: "Level / Progress View", systemImage: "chart.bar.fill", summary: "Determinate, circular, and animated progress indicators."),
        .init(id: .popups, title: "PopUp / Alert / Sheet", systemImage: "rectangle.on.rectangle", summary: "Alerts, confirmation dialogs, and sheets."),
        .init(id: .radioButton, title: "Radio Button", systemImage: "circle.circle", summary: "Mutually exclusive choices using a custom radio group."),
        .init(id: .divider, title: "Separator / Divider", systemImage: "minus", summary: "Section dividers, custom separators, and grouped content."),
        .init(id: .slider, title: "Slider", systemImage: "slider.horizontal.3", summary: "Continuous values, ranges, formatting, and validation."),
        .init(id: .scrollView, title: "ScrollView", systemImage: "scroll", summary: "Vertical and horizontal scrolling with real-world cards."),
        .init(id: .toggle, title: "Switch / Toggle", systemImage: "switch.2", summary: "Boolean settings with persistence-ready state."),
        .init(id: .tabView, title: "Tabs / TabView", systemImage: "square.grid.2x2", summary: "Programmatic tab selection and page-style onboarding."),
        .init(id: .textField, title: "TextField", systemImage: "text.cursor", summary: "Input state, validation, keyboard types, and submit handling."),
        .init(id: .textEditor, title: "TextView / TextEditor", systemImage: "doc.text", summary: "Multi-line text entry with limits and character counts."),
        .init(id: .timePicker, title: "TimePicker", systemImage: "timer", summary: "Time-only picking for reminders and scheduling."),
    ]
}
