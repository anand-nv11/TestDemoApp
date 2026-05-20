import SwiftUI

struct ComponentDetailScreen: View {
    let component: ComponentDemo

    var body: some View {
        Group {
            switch component.id {
            case .box: BoxDemoScreen()
            case .button: ButtonDemoScreen()
            case .calendarPicker: CalendarPickerDemoScreen()
            case .checkbox: CheckboxDemoScreen()
            case .clockPicker: ClockPickerDemoScreen()
            case .colorWell: ColorWellDemoScreen()
            case .datePicker: DatePickerDemoScreen()
            case .fontPicker: FontPickerDemoScreen()
            case .helpButton: HelpButtonDemoScreen()
            case .image: ImageDemoScreen()
            case .label: LabelDemoScreen()
            case .progress: ProgressDemoScreen()
            case .popups: PopupDemoScreen()
            case .radioButton: RadioButtonDemoScreen()
            case .divider: DividerDemoScreen()
            case .slider: SliderDemoScreen()
            case .scrollView: ScrollViewDemoScreen()
            case .toggle: ToggleDemoScreen()
            case .tabView: TabViewDemoScreen()
            case .textField: TextFieldDemoScreen()
            case .textEditor: TextEditorDemoScreen()
            case .timePicker: TimePickerDemoScreen()
            }
        }
        .navigationTitle(component.title)
        .navigationBarTitleDisplayMode(.inline)
    }
}

struct BoxDemoScreen: View {
    @State private var isHighlighted = false

    var body: some View {
        DemoScrollScreen {
            ComponentIntro(title: "Box", text: "SwiftUI does not have a Box type; real apps build boxes from stacks, frames, backgrounds, overlays, and clipping modifiers.")
            DemoSection(title: "Basic Example", explanation: "A plain container with spacing and a system background.") {
                Text("Order #A104 is ready for pickup.")
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding()
                    .background(.background, in: RoundedRectangle(cornerRadius: 12))
            }
            DemoSection(title: "Advanced + Custom Styling", explanation: "Tap to animate a reusable status box area.") {
                Button { isHighlighted.toggle() } label: {
                    VStack(alignment: .leading, spacing: 8) {
                        Label("Warehouse Capacity", systemImage: "shippingbox.fill")
                        ProgressView(value: isHighlighted ? 0.86 : 0.42)
                    }
                    .padding()
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .background(isHighlighted ? Color.green.opacity(0.18) : Color.orange.opacity(0.18), in: RoundedRectangle(cornerRadius: 14))
                    .overlay(RoundedRectangle(cornerRadius: 14).stroke(isHighlighted ? .green : .orange, lineWidth: 1))
                }
                .buttonStyle(.plain)
                .accessibilityLabel("Toggle warehouse capacity highlight")
            }
        }
    }
}

struct ButtonDemoScreen: View {
    @State private var count = 0
    @State private var isLoading = false

    var body: some View {
        DemoScrollScreen {
            ComponentIntro(title: "Button", text: "Use Button for user-triggered actions. Add roles for destructive actions and styles for visual hierarchy.")
            DemoSection(title: "Basic Example", explanation: "A normal button updates local state.") {
                Button("Tap count: \(count)") { count += 1 }
                    .buttonStyle(.borderedProminent)
                    .accessibilityHint("Increments the tap counter")
            }
            DemoSection(title: "Advanced + Animation", explanation: "A production button often has disabled and loading states.") {
                Button {
                    isLoading = true
                    DispatchQueue.main.asyncAfter(deadline: .now() + 1) { isLoading = false }
                } label: {
                    HStack {
                        if isLoading { ProgressView().tint(.white) }
                        Text(isLoading ? "Saving" : "Save Profile")
                    }
                }
                .buttonStyle(PrimaryActionButtonStyle())
                .disabled(isLoading)
                Button("Delete Draft", role: .destructive) { count = 0 }
                    .buttonStyle(.bordered)
            }
        }
    }
}

struct CalendarPickerDemoScreen: View {
    @State private var selectedDates: Set<DateComponents> = []

    var body: some View {
        DemoScrollScreen {
            ComponentIntro(title: "CalendarPicker", text: "Use MultiDatePicker for calendar-style selection of one or more days, such as vacation booking.")
            DemoSection(title: "Basic Example", explanation: "Pick one or more days from a graphical calendar.") {
                MultiDatePicker("Delivery days", selection: $selectedDates)
                    .accessibilityLabel("Delivery days calendar")
            }
            DemoSection(title: "Advanced + Validation", explanation: "A real booking flow validates that at least one date is selected.") {
                StatusPill(text: selectedDates.isEmpty ? "Choose at least one day" : "\(selectedDates.count) day(s) selected", systemImage: selectedDates.isEmpty ? "exclamationmark.triangle" : "checkmark.circle", tint: selectedDates.isEmpty ? .orange : .green)
            }
        }
    }
}

struct CheckboxDemoScreen: View {
    @State private var acceptsMarketing = false
    @State private var acceptsTerms = false

    var body: some View {
        DemoScrollScreen {
            ComponentIntro(title: "Checkbox", text: "SwiftUI Toggle is the native boolean control. A checkbox can be composed from Button when the visual design calls for a square checkmark.")
            DemoSection(title: "Basic Example", explanation: "Reusable checkbox component using Binding state.") {
                CheckBox(title: "Send me product updates", isOn: $acceptsMarketing)
            }
            DemoSection(title: "Advanced + Validation", explanation: "Terms must be checked before checkout can continue.") {
                CheckBox(title: "I accept the terms and privacy policy", isOn: $acceptsTerms)
                Button("Continue") { }
                    .buttonStyle(PrimaryActionButtonStyle())
                    .disabled(!acceptsTerms)
                if !acceptsTerms { Text("Required before continuing.").foregroundStyle(.red).font(.caption) }
            }
        }
    }
}

struct ClockPickerDemoScreen: View {
    @State private var appointmentTime = Date()

    var body: some View {
        DemoScrollScreen {
            ComponentIntro(title: "ClockPicker", text: "Use DatePicker with hourAndMinute when the user only needs to choose a time.")
            DemoSection(title: "Basic Example", explanation: "Compact time picker bound to Date state.") {
                DatePicker("Appointment", selection: $appointmentTime, displayedComponents: .hourAndMinute)
                    .datePickerStyle(.compact)
            }
            DemoSection(title: "Real-world Use Case", explanation: "The selected time can drive reminders, bookings, and availability checks.") {
                Text("Reminder set for \(appointmentTime.formatted(date: .omitted, time: .shortened))")
                    .font(.headline)
            }
        }
    }
}

struct ColorWellDemoScreen: View {
    @State private var brandColor = Color.blue

    var body: some View {
        DemoScrollScreen {
            ComponentIntro(title: "ColorWell", text: "In SwiftUI, ColorPicker is the modern color-well control for choosing colors.")
            DemoSection(title: "Basic Example", explanation: "Bind a color picker to theme state.") {
                ColorPicker("Brand color", selection: $brandColor, supportsOpacity: true)
            }
            DemoSection(title: "Advanced + Custom Styling", explanation: "Preview the selected color in a real product badge.") {
                Label("Premium Plan", systemImage: "sparkles")
                    .padding()
                    .frame(maxWidth: .infinity)
                    .background(brandColor.gradient, in: RoundedRectangle(cornerRadius: 14))
                    .foregroundStyle(.white)
                    .accessibilityLabel("Premium plan badge preview")
            }
        }
    }
}

struct DatePickerDemoScreen: View {
    @State private var startDate = Date()
    private let range = Date()...Calendar.current.date(byAdding: .month, value: 6, to: Date())!

    var body: some View {
        DemoScrollScreen {
            ComponentIntro(title: "DatePicker", text: "DatePicker selects dates, times, or both. Restrict ranges for valid booking and scheduling flows.")
            DemoSection(title: "Basic Example", explanation: "A compact picker for a single date.") {
                DatePicker("Start date", selection: $startDate, in: range, displayedComponents: .date)
            }
            DemoSection(title: "Advanced + Custom Styling", explanation: "Graphical style is useful when calendar context matters.") {
                DatePicker("Vacation start", selection: $startDate, in: range, displayedComponents: .date)
                    .datePickerStyle(.graphical)
                StatusPill(text: "Valid travel date", systemImage: "checkmark.circle")
            }
        }
    }
}

struct FontPickerDemoScreen: View {
    @State private var fontChoice: DemoFontChoice = .body

    var body: some View {
        DemoScrollScreen {
            ComponentIntro(title: "FontPicker", text: "A font picker is commonly modeled as a Picker over approved design-system fonts.")
            DemoSection(title: "Basic Example", explanation: "Choose a font from safe app typography options.") {
                Picker("Font", selection: $fontChoice) {
                    ForEach(DemoFontChoice.allCases) { choice in Text(choice.rawValue).tag(choice) }
                }
                .pickerStyle(.segmented)
            }
            DemoSection(title: "Advanced + Preview", explanation: "Preview content using the selected reusable font value.") {
                Text("The quick brown fox jumps over the lazy dog.")
                    .font(fontChoice.font)
                    .padding()
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .background(.background, in: RoundedRectangle(cornerRadius: 12))
            }
        }
    }
}

struct HelpButtonDemoScreen: View {
    var body: some View {
        DemoScrollScreen {
            ComponentIntro(title: "HelpButton", text: "A small help button gives contextual support without pushing the user away from their current task.")
            DemoSection(title: "Basic + Advanced Example", explanation: "Tap the icon to open a compact popover.") {
                HStack {
                    Text("CVV")
                    HelpButton(title: "Card security code", message: "The CVV is the 3 or 4 digit code printed on your card. It is used only to verify this payment.")
                    Spacer()
                    TextField("123", text: .constant(""))
                        .textFieldStyle(.roundedBorder)
                        .frame(width: 90)
                }
            }
        }
    }
}

struct ImageDemoScreen: View {
    @State private var isFavorite = false

    var body: some View {
        DemoScrollScreen {
            ComponentIntro(title: "Image", text: "Image renders SF Symbols, app assets, or platform images. Always provide accessibility text for meaningful images.")
            DemoSection(title: "Basic Example", explanation: "SF Symbol image with color and scale modifiers.") {
                Image(systemName: "photo.on.rectangle.angled")
                    .font(.system(size: 56))
                    .foregroundStyle(.blue)
                    .accessibilityLabel("Photo stack icon")
            }
            DemoSection(title: "Advanced + Interaction", explanation: "A real product card uses an image-like icon, clipping, overlay, and favorite state.") {
                ZStack(alignment: .topTrailing) {
                    RoundedRectangle(cornerRadius: 16).fill(.teal.gradient).frame(height: 160)
                        .overlay(Image(systemName: "sofa.fill").font(.system(size: 64)).foregroundStyle(.white))
                    Button { isFavorite.toggle() } label: { Image(systemName: isFavorite ? "heart.fill" : "heart") }
                        .buttonStyle(.borderedProminent)
                        .padding()
                        .accessibilityLabel(isFavorite ? "Remove from favorites" : "Add to favorites")
                }
            }
        }
    }
}

struct LabelDemoScreen: View {
    var body: some View {
        DemoScrollScreen {
            ComponentIntro(title: "Label", text: "Label pairs text with an icon and adapts well to menus, lists, settings rows, and accessibility.")
            DemoSection(title: "Basic Example", explanation: "A simple semantic label.") {
                Label("Wi-Fi Connected", systemImage: "wifi")
            }
            DemoSection(title: "Advanced + Custom Styling", explanation: "Labels support style modifiers for icon-only or title-only contexts.") {
                HStack {
                    Label("Paid", systemImage: "checkmark.seal.fill").foregroundStyle(.green)
                    Label("Overdue", systemImage: "clock.badge.exclamationmark.fill").foregroundStyle(.orange)
                    Label("Locked", systemImage: "lock.fill").labelStyle(.iconOnly)
                }
            }
        }
    }
}

struct ProgressDemoScreen: View {
    @State private var progress = 0.35

    var body: some View {
        DemoScrollScreen {
            ComponentIntro(title: "Level / Progress View", text: "ProgressView communicates waiting, completion level, upload progress, and activity state.")
            DemoSection(title: "Basic Example", explanation: "Determinate progress with a numeric value.") {
                ProgressView("Upload", value: progress, total: 1)
                Text(progress.formatted(.percent.precision(.fractionLength(0))))
            }
            DemoSection(title: "Advanced + Animation", explanation: "Animate progress updates and show an indeterminate loader.") {
                HStack { ProgressView().progressViewStyle(.circular); Text("Syncing inventory") }
                Button("Advance") { withAnimation(.snappy) { progress = min(progress + 0.15, 1) } }
                    .buttonStyle(.borderedProminent)
            }
        }
    }
}

struct PopupDemoScreen: View {
    @State private var showAlert = false
    @State private var showSheet = false
    @State private var showDialog = false

    var body: some View {
        DemoScrollScreen {
            ComponentIntro(title: "PopUp / Alert / Sheet", text: "Use alerts for decisions, sheets for focused tasks, and confirmation dialogs for action menus.")
            DemoSection(title: "Basic + Advanced Examples", explanation: "Each presentation is driven by Boolean state.") {
                Button("Show Alert") { showAlert = true }.buttonStyle(.borderedProminent)
                Button("Open Sheet") { showSheet = true }.buttonStyle(.bordered)
                Button("More Actions") { showDialog = true }.buttonStyle(.bordered)
            }
        }
        .alert("Discard draft?", isPresented: $showAlert) {
            Button("Cancel", role: .cancel) { }
            Button("Discard", role: .destructive) { }
        } message: { Text("This cannot be undone.") }
        .sheet(isPresented: $showSheet) { SheetExampleView() }
        .confirmationDialog("Choose action", isPresented: $showDialog) {
            Button("Archive") { }
            Button("Delete", role: .destructive) { }
            Button("Cancel", role: .cancel) { }
        }
    }
}

struct RadioButtonDemoScreen: View {
    @State private var shipping: ShippingSpeed = .standard

    var body: some View {
        DemoScrollScreen {
            ComponentIntro(title: "Radio Button", text: "Radio buttons represent one choice from a small set. Picker is native; a custom row gives radio styling.")
            DemoSection(title: "Basic + Reusable Component", explanation: "Each row writes a value into one shared selection binding.") {
                ForEach(ShippingSpeed.allCases) { option in
                    RadioOption(title: option.rawValue, subtitle: option.detail, value: option, selection: $shipping)
                }
            }
            DemoSection(title: "Real-world Use Case", explanation: "Checkout summary reacts to selected shipping speed.") {
                Text("Selected: \(shipping.rawValue) - \(shipping.detail)")
            }
        }
    }
}

struct DividerDemoScreen: View {
    var body: some View {
        DemoScrollScreen {
            ComponentIntro(title: "Separator / Divider", text: "Divider separates related sections without creating another container.")
            DemoSection(title: "Basic Example", explanation: "A standard divider between rows.") {
                Text("Billing")
                Divider()
                Text("Shipping")
            }
            DemoSection(title: "Advanced + Custom Styling", explanation: "A reusable labeled separator works well in long forms.") {
                CustomSeparator(label: "Payment")
                Rectangle().fill(.secondary.opacity(0.35)).frame(height: 2).clipShape(Capsule())
            }
        }
    }
}

struct SliderDemoScreen: View {
    @State private var budget = 50.0

    var body: some View {
        DemoScrollScreen {
            ComponentIntro(title: "Slider", text: "Slider captures approximate numeric input such as volume, brightness, distance, or budget.")
            DemoSection(title: "Basic Example", explanation: "A bounded value with a visible formatted result.") {
                Slider(value: $budget, in: 0...100, step: 5) { Text("Budget") }
                Text("Budget: \(budget, format: .currency(code: "USD"))")
            }
            DemoSection(title: "Advanced + Validation", explanation: "A real filter can require a minimum value.") {
                StatusPill(text: budget < 20 ? "Minimum budget is $20" : "Budget accepted", systemImage: budget < 20 ? "exclamationmark.triangle" : "checkmark.circle", tint: budget < 20 ? .orange : .green)
            }
        }
    }
}

struct ScrollViewDemoScreen: View {
    private let categories = ["Coffee", "Bakery", "Lunch", "Dinner", "Dessert"]

    var body: some View {
        DemoScrollScreen {
            ComponentIntro(title: "ScrollView", text: "ScrollView is useful for custom scrolling content. Prefer List for very large data sets with cell reuse.")
            DemoSection(title: "Basic Example", explanation: "Horizontal scrolling chips.") {
                ScrollView(.horizontal, showsIndicators: false) {
                    HStack { ForEach(categories, id: \.self) { Text($0).padding(.horizontal, 14).padding(.vertical, 8).background(.quaternary, in: Capsule()) } }
                }
            }
            DemoSection(title: "Advanced + Real-world Use Case", explanation: "A responsive vertical feed of cards.") {
                ForEach(categories, id: \.self) { category in
                    InfoBox(title: category, systemImage: "fork.knife") { Text("Popular nearby options updated today.").foregroundStyle(.secondary) }
                }
            }
        }
    }
}

struct ToggleDemoScreen: View {
    @State private var notifications = true
    @State private var faceID = false

    var body: some View {
        DemoScrollScreen {
            ComponentIntro(title: "Switch / Toggle", text: "Toggle is the standard Boolean control. It supports switch and button-like styles depending on platform context.")
            DemoSection(title: "Basic Example", explanation: "A standard settings switch.") {
                Toggle("Push notifications", isOn: $notifications)
            }
            DemoSection(title: "Advanced + State Handling", explanation: "Grouped settings update independent state values.") {
                Toggle(isOn: $faceID) { Label("Use Face ID", systemImage: "faceid") }
                Toggle("Email receipts", isOn: .constant(true)).toggleStyle(.button)
            }
        }
    }
}

struct TabViewDemoScreen: View {
    @State private var selectedTab: DemoTab = .overview

    var body: some View {
        DemoScrollScreen {
            ComponentIntro(title: "Tabs / TabView", text: "TabView organizes top-level app areas or page-style content. Use selection for programmatic control.")
            DemoSection(title: "Basic + Advanced Example", explanation: "Programmatic selection with three reusable tab values.") {
                Picker("Selected tab", selection: $selectedTab) {
                    ForEach(DemoTab.allCases) { Text($0.rawValue).tag($0) }
                }
                .pickerStyle(.segmented)
                TabView(selection: $selectedTab) {
                    ForEach(DemoTab.allCases) { tab in
                        Text(tab.rawValue).font(.title2.bold()).tag(tab)
                    }
                }
                .frame(height: 140)
                .tabViewStyle(.page(indexDisplayMode: .always))
            }
        }
    }
}

struct TextFieldDemoScreen: View {
    @State private var viewModel = DemoFormViewModel()

    var body: some View {
        @Bindable var viewModel = viewModel
        DemoScrollScreen {
            ComponentIntro(title: "TextField", text: "TextField captures short text. Validate early, label clearly, and choose the right keyboard/content type.")
            DemoSection(title: "Basic Example", explanation: "Two-way text binding with submit handling.") {
                TextField("Full name", text: $viewModel.profile.name)
                    .textFieldStyle(.roundedBorder)
                    .textContentType(.name)
                    .accessibilityLabel("Full name")
            }
            DemoSection(title: "Advanced + Validation", explanation: "A real sign-up form validates name, email, and terms.") {
                TextField("Email", text: $viewModel.profile.email)
                    .textFieldStyle(.roundedBorder)
                    .textInputAutocapitalization(.never)
                    .keyboardType(.emailAddress)
                    .accessibilityLabel("Email address")
                CheckBox(title: "I accept the terms", isOn: $viewModel.profile.acceptedTerms)
                Button("Create Account") { _ = viewModel.submitProfile() }
                    .buttonStyle(PrimaryActionButtonStyle())
                    .disabled(!viewModel.isProfileValid)
                if !viewModel.isProfileValid { Text("Enter a valid name, email, and accept terms.").foregroundStyle(.red).font(.caption) }
            }
        }
    }
}

struct TextEditorDemoScreen: View {
    @State private var viewModel = DemoFormViewModel()

    var body: some View {
        @Bindable var viewModel = viewModel
        DemoScrollScreen {
            ComponentIntro(title: "TextView / TextEditor", text: "TextEditor is SwiftUI's multi-line text input for notes, bios, and messages.")
            DemoSection(title: "Basic Example", explanation: "A multi-line editor with a fixed height.") {
                TextEditor(text: $viewModel.notes)
                    .frame(minHeight: 140)
                    .padding(8)
                    .background(.background, in: RoundedRectangle(cornerRadius: 12))
                    .overlay(RoundedRectangle(cornerRadius: 12).stroke(.quaternary))
                    .accessibilityLabel("Order notes")
            }
            DemoSection(title: "Advanced + Validation", explanation: "Character limits are common in support and profile flows.") {
                HStack {
                    Text("\(viewModel.notes.count)/180")
                    Spacer()
                    if let message = viewModel.notesValidationMessage { Text(message).foregroundStyle(.red) }
                }
                .font(.caption)
            }
        }
    }
}

struct TimePickerDemoScreen: View {
    @State private var reminderTime = Date()

    var body: some View {
        DemoScrollScreen {
            ComponentIntro(title: "TimePicker", text: "A time picker is DatePicker limited to hour and minute components.")
            DemoSection(title: "Basic Example", explanation: "Wheel style makes time selection explicit.") {
                DatePicker("Reminder time", selection: $reminderTime, displayedComponents: .hourAndMinute)
                    .datePickerStyle(.wheel)
                    .labelsHidden()
            }
            DemoSection(title: "Real-world Use Case", explanation: "Show the selected reminder in readable text.") {
                Label("Daily reminder at \(reminderTime.formatted(date: .omitted, time: .shortened))", systemImage: "bell")
            }
        }
    }
}

struct DemoScrollScreen<Content: View>: View {
    @ViewBuilder var content: Content

    var body: some View {
        ScrollView {
            LazyVStack(alignment: .leading, spacing: 16) {
                content
            }
            .padding()
            .frame(maxWidth: 720, alignment: .leading)
            .frame(maxWidth: .infinity)
        }
        .background(Color(.systemGroupedBackground))
    }
}

struct SheetExampleView: View {
    @Environment(\.dismiss) private var dismiss
    @State private var message = ""

    var body: some View {
        NavigationStack {
            Form {
                TextField("Message", text: $message)
                Text("Sheets are good for short focused tasks, such as sending feedback.")
                    .foregroundStyle(.secondary)
            }
            .navigationTitle("Feedback")
            .toolbar { Button("Done") { dismiss() } }
        }
    }
}

struct ComponentScreens_Previews: PreviewProvider {
    static var previews: some View {
        NavigationStack {
            ComponentDetailScreen(component: DemoRepository.components[0])
        }
    }
}
