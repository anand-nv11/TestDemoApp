import SwiftUI

struct ContentView: View {
    var body: some View {
        TabView {
            ComponentCatalogScreen()
                .tabItem { Label("Components", systemImage: "square.grid.2x2") }

            SwiftUIGuideScreen()
                .tabItem { Label("Guide", systemImage: "book") }
        }
        .accessibilityIdentifier("RootTabView")
    }
}

struct ComponentCatalogScreen: View {
    @State private var searchText = "" // variable create

    private var filteredComponents: [ComponentDemo] {
        guard !searchText.isEmpty else { return DemoRepository.components }
        return DemoRepository.components.filter {
            $0.title.localizedCaseInsensitiveContains(searchText) ||
            $0.summary.localizedCaseInsensitiveContains(searchText)
        }
    }

    var body: some View {
        NavigationStack {
            List(filteredComponents) { component in
                NavigationLink(value: component) {
                    HStack(spacing: 12) {
                        Image(systemName: component.systemImage)
                            .frame(width: 28)
                            .foregroundStyle(.tint)
                            .accessibilityHidden(true)

                        VStack(alignment: .leading, spacing: 4) {
                            Text(component.title)
                                .font(.headline)
                            Text(component.summary)
                                .font(.subheadline)
                                .foregroundStyle(.secondary)
                        }

                    }
                    .padding(.vertical, 6)
                }
                .accessibilityLabel("Open \(component.title)")
                .accessibilityIdentifier("ComponentRow_\(component.id.rawValue)")
            }
            .navigationTitle("SwiftUI Components")
            .searchable(text: $searchText, prompt: "Search components")
            .navigationDestination(for: ComponentDemo.self) { component in
                ComponentDetailScreen(component: component)
                    .navigationTitle(component.title)
                    .navigationBarTitleDisplayMode(.inline)
            }
            .overlay {
                if filteredComponents.isEmpty {
                    ContentUnavailableView("No components", systemImage: "magnifyingglass", description: Text("Try a different search term."))
                }
            }
        }
    }
}

struct SwiftUIGuideScreen: View {
    var body: some View {
        NavigationStack {
            ScrollView {
                LazyVStack(alignment: .leading, spacing: 16) {
                    InfoBox(title: "Project Structure", systemImage: "folder") {
                        Text("Models hold simple data, ViewModels own validation/state, reusable controls live in Views/Components, and demo screens live in Views/Screens.")
                    }
                    GuideSection(title: "Best Practices", items: [
                        "Use NavigationStack for modern navigation.",
                        "Keep view state private and move form/business validation into view models.",
                        "Prefer native controls before custom controls.",
                        "Use dynamic colors/materials so light and dark mode work automatically.",
                        "Add accessibility labels, values, hints, and identifiers where interaction matters."
                    ])
                    GuideSection(title: "Common Interview Questions", items: [
                        "What is the difference between @State, @Binding, @Observable, and @Environment?",
                        "When should you use List instead of ScrollView?",
                        "How does SwiftUI decide when to refresh a view?",
                        "How do NavigationStack and navigationDestination differ from NavigationView?",
                        "How do you test SwiftUI validation logic and UI flows?"
                    ])
                    GuideSection(title: "Common Mistakes", items: [
                        "Putting networking or validation directly in large views.",
                        "Using fixed widths that break on small screens or Dynamic Type.",
                        "Forgetting labels for icon-only buttons.",
                        "Building custom controls when Toggle, Picker, DatePicker, or Button already solve the problem.",
                        "Using ScrollView for thousands of rows where List is more appropriate."
                    ])
                    GuideSection(title: "Performance Tips", items: [
                        "Use LazyVStack/LazyHStack for custom scrolling collections.",
                        "Keep model values stable and identifiable in ForEach.",
                        "Move expensive formatting or derived state out of deeply repeated views.",
                        "Use images at appropriate sizes and avoid unnecessary overlays in large lists.",
                        "Run Instruments when performance matters; do not optimize by guesswork."
                    ])
                    GuideSection(title: "Testing Examples", items: [
                        "Unit tests verify DemoFormViewModel validation and state transitions.",
                        "UI tests launch the app, find the catalog, and open a component detail screen.",
                        "Keep most logic testable outside SwiftUI views."
                    ])
                }
                .padding()
                .frame(maxWidth: 760, alignment: .leading)
                .frame(maxWidth: .infinity)
            }
            .background(Color(.systemGroupedBackground))
            .navigationTitle("SwiftUI Guide")
        }
    }
}

struct GuideSection: View {
    let title: String
    let items: [String]

    var body: some View {
        DemoSection(title: title, explanation: "Reference notes for production SwiftUI work.") {
            VStack(alignment: .leading, spacing: 8) {
                ForEach(items, id: \.self) { item in
                    Label(item, systemImage: "checkmark.circle")
                        .labelStyle(.titleAndIcon)
                        .font(.subheadline)
                }
            }
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
        ContentView()
            .preferredColorScheme(.dark)
    }
}

struct ComponentCatalogScreen_Previews: PreviewProvider {
    static var previews: some View {
        ComponentCatalogScreen()
    }
}

struct SwiftUIGuideScreen_Previews: PreviewProvider {
    static var previews: some View {
        SwiftUIGuideScreen()
    }
}
// These support types live in ContentView.swift because this file is already a member
// of the app target. The separate demo files can be moved back once target membership
// is enabled in Xcode's File Inspector.
struct ComponentDemo: Identifiable, Hashable {
    let id: ComponentKind
    let title: String
    let systemImage: String
    let summary: String
}

enum ComponentKind: String, CaseIterable, Identifiable, Hashable {
    case box, button, calendarPicker, checkbox, clockPicker, colorWell, datePicker, fontPicker, helpButton, image, label, progress, popups, radioButton, divider, slider, scrollView, toggle, tabView, textField, textEditor, timePicker

    var id: String { rawValue }
}

struct DemoRepository {
    static let components: [ComponentDemo] = [
        .init(id: .box, title: "Box", systemImage: "shippingbox", summary: "Containers, cards, backgrounds, spacing, and responsive layout."),
        .init(id: .button, title: "Button", systemImage: "hand.tap", summary: "Actions, roles, custom styles, loading state, and animation."),
        .init(id: .calendarPicker, title: "CalendarPicker", systemImage: "calendar", summary: "Single and multi-date calendar selection for scheduling flows."),
        .init(id: .checkbox, title: "Checkbox", systemImage: "checkmark.square", summary: "Reusable checkbox state for forms and agreements."),
        .init(id: .clockPicker, title: "ClockPicker", systemImage: "clock", summary: "Hour and minute selection with appointment validation."),
        .init(id: .colorWell, title: "ColorWell", systemImage: "paintpalette", summary: "ColorPicker examples for theme customization."),
        .init(id: .datePicker, title: "DatePicker", systemImage: "calendar.badge.clock", summary: "Date ranges, graphical style, and booking validation."),
        .init(id: .fontPicker, title: "FontPicker", systemImage: "textformat", summary: "Reusable font choice picker for readable typography."),
        .init(id: .helpButton, title: "HelpButton", systemImage: "questionmark.circle", summary: "Contextual help with popovers and accessibility."),
        .init(id: .image, title: "Image", systemImage: "photo", summary: "System and asset images with resizing and labels."),
        .init(id: .label, title: "Label", systemImage: "tag", summary: "Text plus icon labels, styles, and status rows."),
        .init(id: .progress, title: "Level / Progress View", systemImage: "chart.bar.fill", summary: "Determinate, circular, and animated progress indicators."),
        .init(id: .popups, title: "PopUp / Alert / Sheet", systemImage: "rectangle.on.rectangle", summary: "Alerts, dialogs, and sheets."),
        .init(id: .radioButton, title: "Radio Button", systemImage: "circle.circle", summary: "Mutually exclusive choices using a custom radio group."),
        .init(id: .divider, title: "Separator / Divider", systemImage: "minus", summary: "Section dividers, custom separators, and grouped content."),
        .init(id: .slider, title: "Slider", systemImage: "slider.horizontal.3", summary: "Continuous values, ranges, formatting, and validation."),
        .init(id: .scrollView, title: "ScrollView", systemImage: "scroll", summary: "Vertical and horizontal scrolling with real-world cards."),
        .init(id: .toggle, title: "Switch / Toggle", systemImage: "switch.2", summary: "Boolean settings with state handling."),
        .init(id: .tabView, title: "Tabs / TabView", systemImage: "square.grid.2x2", summary: "Programmatic tab selection and page-style onboarding."),
        .init(id: .textField, title: "TextField", systemImage: "text.cursor", summary: "Input state, validation, keyboard types, and submit handling."),
        .init(id: .textEditor, title: "TextView / TextEditor", systemImage: "doc.text", summary: "Multi-line text entry with limits and character counts."),
        .init(id: .timePicker, title: "TimePicker", systemImage: "timer", summary: "Time-only picking for reminders and scheduling.")
    ]
}

struct DemoSection<Content: View>: View {
    let title: String
    let explanation: String
    @ViewBuilder var content: Content

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text(title).font(.headline)
            Text(explanation).font(.subheadline).foregroundStyle(.secondary)
            content
        }
        .padding()
        .background(.thinMaterial, in: RoundedRectangle(cornerRadius: 14, style: .continuous))
    }
}

struct InfoBox<Content: View>: View {
    let title: String
    let systemImage: String
    @ViewBuilder var content: Content

    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            Label(title, systemImage: systemImage).font(.headline)
            content
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding()
        .background(Color.accentColor.opacity(0.12), in: RoundedRectangle(cornerRadius: 12, style: .continuous))
    }
}

struct ComponentDetailScreen: View {
    let component: ComponentDemo
    @State private var isEnabled = true
    @State private var sliderValue = 0.5
    @State private var text = ""
    @State private var selectedDate = Date()
    @State private var selectedColor = Color.blue
    @State private var selectedOption = "Standard"
    @State private var selectedTab = 0
    @State private var showAlert = false
    @State private var showSheet = false

    var body: some View {
        ScrollView {
            LazyVStack(alignment: .leading, spacing: 16) {
                InfoBox(title: component.title, systemImage: component.systemImage) {
                    Text(component.summary)
                        .foregroundStyle(.secondary)
                }

                componentUI
            }
            .padding()
            .frame(maxWidth: 720, alignment: .leading)
            .frame(maxWidth: .infinity)
        }
        .background(Color(.systemGroupedBackground))
        .alert("Action completed", isPresented: $showAlert) {
            Button("OK", role: .cancel) { }
        } message: {
            Text("This is a sample alert for the selected component.")
        }
        .sheet(isPresented: $showSheet) {
            NavigationStack {
                Text("This is a sample sheet.")
                    .padding()
                    .navigationTitle("Sheet")
                    .toolbar { Button("Done") { showSheet = false } }
            }
        }
    }

    @ViewBuilder private var componentUI: some View {
        switch component.id {
        case .button:
            ShowcaseCard("Button Types") {
                Button("Default Button") { showAlert = true }
                Button("Prominent Button") { showAlert = true }
                    .buttonStyle(.borderedProminent)
                Button("Bordered Button") { showAlert = true }
                    .buttonStyle(.bordered)
                Button("Destructive Button", role: .destructive) { showAlert = true }
                    .buttonStyle(.bordered)
                Button { showAlert = true } label: {
                    Label("Icon Button", systemImage: "paperplane.fill")
                }
                .buttonStyle(.borderedProminent)
            }

        case .box:
            ShowcaseCard("Box Layouts") {
                Text("Plain box").padding().frame(maxWidth: .infinity).background(.quaternary, in: RoundedRectangle(cornerRadius: 12))
                Text("Outlined box").padding().frame(maxWidth: .infinity).overlay(RoundedRectangle(cornerRadius: 12).stroke(.blue))
                Label("Status box", systemImage: "checkmark.circle.fill").padding().frame(maxWidth: .infinity, alignment: .leading).background(.red.opacity(0.15), in: RoundedRectangle(cornerRadius: 12))
            }

        case .calendarPicker, .datePicker:
            ShowcaseCard("Date UI") {
                DatePicker("Compact date", selection: $selectedDate, displayedComponents: .date)
                DatePicker("Graphical date", selection: $selectedDate, displayedComponents: .date)
                    .datePickerStyle(.graphical)
            }

        case .clockPicker, .timePicker:
            ShowcaseCard("Time UI") {
                DatePicker("Compact time", selection: $selectedDate, displayedComponents: .hourAndMinute)
                DatePicker("Wheel time", selection: $selectedDate, displayedComponents: .hourAndMinute)
                    .datePickerStyle(.wheel)
                    .labelsHidden()
            }

        case .checkbox, .toggle:
            ShowcaseCard("Boolean Controls") {
                Toggle("Notifications", isOn: $isEnabled)
                Toggle("Button Toggle", isOn: $isEnabled).toggleStyle(.button)
                Button {
                    isEnabled.toggle()
                } label: {
                    Label("Custom Checkbox", systemImage: isEnabled ? "checkmark.square.fill" : "square")
                }
                .buttonStyle(.plain)
            }

        case .colorWell:
            ShowcaseCard("Color Pickers") {
                ColorPicker("Brand color", selection: $selectedColor)
                RoundedRectangle(cornerRadius: 14).fill(selectedColor.gradient).frame(height: 90)
                Label("Preview Badge", systemImage: "sparkles").padding().background(selectedColor.opacity(0.18), in: Capsule())
            }

        case .fontPicker:
            ShowcaseCard("Font Styles") {
                Text("Default font").font(.body)
                Text("Rounded font").font(.system(.title3, design: .rounded))
                Text("Serif font").font(.system(.title3, design: .serif))
                Text("Monospaced font").font(.system(.title3, design: .monospaced))
            }

        case .helpButton:
            ShowcaseCard("Help UI") {
                Label("Inline help text", systemImage: "questionmark.circle")
                Button { showAlert = true } label: { Label("Show Help Alert", systemImage: "info.circle") }
                    .buttonStyle(.bordered)
                Text("Use help buttons beside fields that need short explanations.").foregroundStyle(.secondary)
            }

        case .image:
            ShowcaseCard("Image UI") {
                Image(systemName: "photo.fill").font(.system(size: 52)).foregroundStyle(.blue)
                Image(systemName: "person.crop.circle.fill").font(.system(size: 64)).foregroundStyle(.purple)
                RoundedRectangle(cornerRadius: 16).fill(.teal.gradient).frame(height: 120).overlay(Image(systemName: "star.fill").font(.largeTitle).foregroundStyle(.white))
            }

        case .label:
            ShowcaseCard("Label UI") {
                Label("Paid", systemImage: "checkmark.seal.fill").foregroundStyle(.green)
                Label("Pending", systemImage: "clock.fill").foregroundStyle(.orange)
                Label("Locked", systemImage: "lock.fill").labelStyle(.iconOnly).font(.title2)
            }

        case .progress:
            ShowcaseCard("Progress UI") {
                ProgressView("Upload", value: sliderValue)
                ProgressView(value: sliderValue).progressViewStyle(.circular)
                Button("Advance") { withAnimation { sliderValue = sliderValue >= 1 ? 0 : min(sliderValue + 0.15, 1) } }.buttonStyle(.borderedProminent)
            }

        case .popups:
            ShowcaseCard("Presentation UI") {
                Button("Show Alert") { showAlert = true }.buttonStyle(.borderedProminent)
                Button("Show Sheet") { showSheet = true }.buttonStyle(.bordered)
                Button("Cancel Role", role: .cancel) { }.buttonStyle(.bordered)
            }

        case .radioButton:
            ShowcaseCard("Radio Choices") {
                Picker("Shipping", selection: $selectedOption) {
                    Text("Standard").tag("Standard")
                    Text("Express").tag("Express")
                    Text("Overnight").tag("Overnight")
                }
                .pickerStyle(.segmented)
                ForEach(["Standard", "Express", "Overnight"], id: \.self) { option in
                    Button { selectedOption = option } label: {
                        Label(option, systemImage: selectedOption == option ? "largecircle.fill.circle" : "circle")
                    }
                    .buttonStyle(.plain)
                }
            }

        case .divider:
            ShowcaseCard("Divider UI") {
                Text("Account")
                Divider()
                Text("Billing")
                HStack { Divider(); Text("OR").font(.caption).foregroundStyle(.secondary); Divider() }
                Rectangle().fill(.secondary.opacity(0.3)).frame(height: 2).clipShape(Capsule())
            }

        case .slider:
            ShowcaseCard("Slider UI") {
                Slider(value: $sliderValue)
                Slider(value: $sliderValue, in: 0...1, step: 0.1) { Text("Volume") }
                Text("Value: \(sliderValue.formatted(.percent.precision(.fractionLength(0))))")
            }

        case .scrollView:
            ShowcaseCard("Scroll UI") {
                ScrollView(.horizontal, showsIndicators: false) { HStack { ForEach(["Coffee", "Lunch", "Dinner", "Dessert"], id: \.self) { Text($0).padding(.horizontal, 14).padding(.vertical, 8).background(.quaternary, in: Capsule()) } } }
                ScrollView { VStack(alignment: .leading) { ForEach(1...4, id: \.self) { Text("Vertical row \($0)").padding().frame(maxWidth: .infinity, alignment: .leading).background(.background, in: RoundedRectangle(cornerRadius: 10)) } } }.frame(height: 190)
            }

        case .tabView:
            ShowcaseCard("Tab UI") {
                Picker("Tab", selection: $selectedTab) { Text("Home").tag(0); Text("Stats").tag(1); Text("Settings").tag(2) }.pickerStyle(.segmented)
                TabView(selection: $selectedTab) { Text("Home").tag(0); Text("Stats").tag(1); Text("Settings").tag(2) }.frame(height: 130).tabViewStyle(.page)
            }

        case .textField:
            ShowcaseCard("TextField UI") {
                TextField("Full name", text: $text).textFieldStyle(.roundedBorder)
                TextField("Email", text: $text).textFieldStyle(.roundedBorder).keyboardType(.emailAddress).textInputAutocapitalization(.never)
                SecureField("Password", text: $text).textFieldStyle(.roundedBorder)
            }

        case .textEditor:
            ShowcaseCard("TextEditor UI") {
                TextEditor(text: $text).frame(height: 120).padding(8).background(.background, in: RoundedRectangle(cornerRadius: 12)).overlay(RoundedRectangle(cornerRadius: 12).stroke(.quaternary))
                Text("Characters: \(text.count)").font(.caption).foregroundStyle(.secondary)
            }
        }
    }
}

struct ShowcaseCard<Content: View>: View {
    let title: String
    @ViewBuilder var content: Content

    init(_ title: String, @ViewBuilder content: () -> Content) {
        self.title = title
        self.content = content()
    }

    var body: some View {
        VStack(alignment: .leading, spacing: 14) {
            Text(title)
                .font(.headline)
            content
        }
        .padding()
        .frame(maxWidth: .infinity, alignment: .leading)
        .background(.thinMaterial, in: RoundedRectangle(cornerRadius: 14, style: .continuous))
    }
}

