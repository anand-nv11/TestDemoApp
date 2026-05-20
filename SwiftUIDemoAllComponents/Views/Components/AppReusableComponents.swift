import SwiftUI

struct DemoSection<Content: View>: View {
    let title: String
    let explanation: String
    @ViewBuilder var content: Content

    var body: some View {
        VStack(alignment: .leading, spacing: 12) {
            VStack(alignment: .leading, spacing: 4) {
                Text(title)
                    .font(.headline)
                Text(explanation)
                    .font(.subheadline)
                    .foregroundStyle(.secondary)
            }
            content
        }
        .padding()
        .background(.thinMaterial, in: RoundedRectangle(cornerRadius: 14, style: .continuous))
        .accessibilityElement(children: .contain)
    }
}

struct InfoBox<Content: View>: View {
    let title: String
    let systemImage: String
    @ViewBuilder var content: Content

    var body: some View {
        VStack(alignment: .leading, spacing: 10) {
            Label(title, systemImage: systemImage)
                .font(.headline)
            content
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding()
        .background(Color.accentColor.opacity(0.12), in: RoundedRectangle(cornerRadius: 12, style: .continuous))
    }
}

struct PrimaryActionButtonStyle: ButtonStyle {
    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .font(.headline)
            .frame(maxWidth: .infinity)
            .padding(.vertical, 12)
            .background(configuration.isPressed ? Color.accentColor.opacity(0.7) : Color.accentColor)
            .foregroundStyle(.white)
            .clipShape(RoundedRectangle(cornerRadius: 12, style: .continuous))
            .scaleEffect(configuration.isPressed ? 0.98 : 1)
            .animation(.snappy(duration: 0.18), value: configuration.isPressed)
    }
}

struct StatusPill: View {
    let text: String
    let systemImage: String
    var tint: Color = .green

    var body: some View {
        Label(text, systemImage: systemImage)
            .font(.caption.weight(.semibold))
            .padding(.horizontal, 10)
            .padding(.vertical, 6)
            .background(tint.opacity(0.15), in: Capsule())
            .foregroundStyle(tint)
            .accessibilityLabel(text)
    }
}

struct CheckBox: View {
    let title: String
    @Binding var isOn: Bool

    var body: some View {
        Button {
            isOn.toggle()
        } label: {
            Label(title, systemImage: isOn ? "checkmark.square.fill" : "square")
                .symbolRenderingMode(.hierarchical)
                .frame(maxWidth: .infinity, alignment: .leading)
        }
        .buttonStyle(.plain)
        .foregroundStyle(isOn ? .accent : .primary)
        .accessibilityLabel(title)
        .accessibilityValue(isOn ? "Checked" : "Unchecked")
        .accessibilityAddTraits(.isButton)
    }
}

struct RadioOption<Value: Hashable>: View {
    let title: String
    let subtitle: String
    let value: Value
    @Binding var selection: Value

    var body: some View {
        Button {
            selection = value
        } label: {
            HStack(spacing: 12) {
                Image(systemName: selection == value ? "largecircle.fill.circle" : "circle")
                    .foregroundStyle(selection == value ? .accent : .secondary)
                VStack(alignment: .leading) {
                    Text(title).font(.body.weight(.medium))
                    Text(subtitle).font(.caption).foregroundStyle(.secondary)
                }
                Spacer()
            }
            .padding(10)
            .background(selection == value ? Color.accentColor.opacity(0.12) : Color.clear, in: RoundedRectangle(cornerRadius: 10))
        }
        .buttonStyle(.plain)
        .accessibilityLabel(title)
        .accessibilityValue(selection == value ? "Selected" : "Not selected")
    }
}

struct HelpButton: View {
    let title: String
    let message: String
    @State private var isPresented = false

    var body: some View {
        Button {
            isPresented.toggle()
        } label: {
            Image(systemName: "questionmark.circle")
                .imageScale(.large)
        }
        .buttonStyle(.borderless)
        .popover(isPresented: $isPresented) {
            VStack(alignment: .leading, spacing: 8) {
                Text(title).font(.headline)
                Text(message).font(.body)
            }
            .padding()
            .presentationCompactAdaptation(.popover)
        }
        .accessibilityLabel("Help for \(title)")
    }
}

struct CustomSeparator: View {
    var label: String? = nil

    var body: some View {
        HStack(spacing: 12) {
            Divider()
            if let label {
                Text(label)
                    .font(.caption)
                    .foregroundStyle(.secondary)
                    .textCase(.uppercase)
            }
            Divider()
        }
        .accessibilityHidden(label == nil)
        .accessibilityLabel(label ?? "Separator")
    }
}

struct ComponentIntro: View {
    let title: String
    let text: String

    var body: some View {
        InfoBox(title: title, systemImage: "lightbulb") {
            Text(text)
                .font(.subheadline)
                .foregroundStyle(.secondary)
        }
    }
}

struct ReusableComponents_Previews: PreviewProvider {
    static var previews: some View {
        VStack(spacing: 16) {
            DemoSection(title: "Preview", explanation: "Reusable section styling.") {
                StatusPill(text: "Ready", systemImage: "checkmark.circle")
            }
            CheckBox(title: "Accept terms", isOn: .constant(true))
            RadioOption(title: "Express", subtitle: "2 business days", value: ShippingSpeed.express, selection: .constant(.express))
        }
        .padding()
    }
}
