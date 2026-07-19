import SwiftUI
#if canImport(UIKit)
import UIKit
#endif

struct LoginScreen: View {
    @State private var email = ""
    @State private var password = ""
    @State private var rememberMe = false
    @State private var showPassword = false
    @State private var didAttemptLogin = false
    @State private var errorMessage = ""
    @State private var isLoggedIn = false
    @FocusState private var focusedField: LoginField?

    private var trimmedEmail: String {
        email.trimmingCharacters(in: .whitespacesAndNewlines)
    }

    private var emailValidationMessage: String? {
        if trimmedEmail.isEmpty {
            return "Email is required."
        }

        if email.contains(" ") {
            return "Email cannot contain spaces."
        }

        let pattern = #"^[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$"#
        if trimmedEmail.range(of: pattern, options: .regularExpression) == nil {
            return "Enter a valid email address."
        }

        return nil
    }

    private var passwordRequirements: [PasswordRequirement] {
        [
            PasswordRequirement(title: "At least 8 characters", isMet: password.count >= 8),
            PasswordRequirement(title: "One uppercase letter", isMet: password.range(of: #"[A-Z]"#, options: .regularExpression) != nil),
            PasswordRequirement(title: "One lowercase letter", isMet: password.range(of: #"[a-z]"#, options: .regularExpression) != nil),
            PasswordRequirement(title: "One number", isMet: password.range(of: #"[0-9]"#, options: .regularExpression) != nil),
            PasswordRequirement(title: "One special character", isMet: password.range(of: #"[^A-Za-z0-9]"#, options: .regularExpression) != nil),
            PasswordRequirement(title: "No spaces", isMet: !password.contains(" "))
        ]
    }

    private var isPasswordValid: Bool {
        passwordRequirements.allSatisfy(\.isMet)
    }

    private var isFormValid: Bool {
        emailValidationMessage == nil && isPasswordValid
    }

    var body: some View {
        if isLoggedIn {
            ContentView()
                .accessibilityIdentifier("homeScreen")
        } else {
            NavigationStack {
                ScrollView {
                    VStack(spacing: 24) {
                        header
                        loginForm
                    }
                    .padding()
                    .frame(maxWidth: 520)
                    .frame(maxWidth: .infinity)
                }
                .background(Color(.systemGroupedBackground))
                .navigationTitle("Login")
                .navigationBarTitleDisplayMode(.inline)
                .accessibilityIdentifier("loginScreen")
            }
        }
    }

    private var header: some View {
        VStack(spacing: 12) {
            Image(systemName: "lock.shield.fill")
                .font(.system(size: 52))
                .foregroundStyle(.tint)
                .accessibilityHidden(true)

            Text("Welcome Back")
                .font(.largeTitle.weight(.bold))

            Text("Sign in with a valid email and a strong password to continue.")
                .font(.subheadline)
                .foregroundStyle(.secondary)
                .multilineTextAlignment(.center)
        }
        .padding(.top, 24)
    }

    private var loginForm: some View {
        VStack(alignment: .leading, spacing: 18) {
            VStack(alignment: .leading, spacing: 8) {
                Text("Email")
                    .font(.headline)

                TextField("Email", text: $email)
                    .textInputAutocapitalization(.never)
                    .keyboardType(.emailAddress)
                    .textContentType(.username)
                    .autocorrectionDisabled()
                    .focused($focusedField, equals: .email)
                    .submitLabel(.next)
                    .onSubmit { focusedField = .password }
                    .textFieldStyle(.roundedBorder)
                    .accessibilityIdentifier("loginEmailField")

                if didAttemptLogin, let emailValidationMessage {
                    ValidationMessage(text: emailValidationMessage)
                }
            }

            VStack(alignment: .leading, spacing: 8) {
                Text("Password")
                    .font(.headline)

                HStack(spacing: 8) {
                    Group {
                        if showPassword {
                            TextField("Password", text: $password)
                        } else {
                            SecureField("Password", text: $password)
                        }
                    }
                    .textContentType(.password)
                    .focused($focusedField, equals: .password)
                    .submitLabel(.go)
                    .onSubmit { login() }
                    .accessibilityIdentifier("loginPasswordField")

                    Button {
                        showPassword.toggle()
                    } label: {
                        Image(systemName: showPassword ? "eye.slash" : "eye")
                            .frame(width: 28, height: 28)
                    }
                    .buttonStyle(.borderless)
                    .accessibilityIdentifier("showPasswordButton")
                    .accessibilityLabel(showPassword ? "Hide password" : "Show password")
                }
                .padding(8)
                .background(.background, in: RoundedRectangle(cornerRadius: 8))
                .overlay(RoundedRectangle(cornerRadius: 8).stroke(Color(.separator)))

                VStack(alignment: .leading, spacing: 6) {
                    ForEach(passwordRequirements) { requirement in
                        Label(requirement.title, systemImage: requirement.isMet ? "checkmark.circle.fill" : "circle")
                            .font(.caption)
                            .foregroundStyle(requirement.isMet ? .green : .secondary)
                    }
                }
                .accessibilityElement(children: .contain)
            }

            Toggle("Remember me", isOn: $rememberMe)
                .accessibilityIdentifier("rememberMeToggle")

            Button("Login") {
                login()
            }
            .font(.headline)
            .frame(maxWidth: .infinity)
            .buttonStyle(.borderedProminent)
            .controlSize(.large)
            .disabled(!isFormValid)
            .accessibilityIdentifier("loginButton")

            Text(errorMessage)
                .font(.footnote)
                .foregroundStyle(.red)
                .frame(maxWidth: .infinity, alignment: .leading)
                .accessibilityIdentifier("loginErrorMessage")
        }
        .padding()
        .background(.thinMaterial, in: RoundedRectangle(cornerRadius: 14, style: .continuous))
    }

    private func login() {
        didAttemptLogin = true

        guard isFormValid else {
            errorMessage = emailValidationMessage ?? "Password does not meet all security requirements."
            return
        }

        errorMessage = ""
        focusedField = nil
        #if canImport(UIKit)
        UIApplication.shared.sendAction(#selector(UIResponder.resignFirstResponder), to: nil, from: nil, for: nil)
        #endif

        DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) {
            isLoggedIn = true
        }
    }
}

private enum LoginField: Hashable {
    case email
    case password
}

private struct PasswordRequirement: Identifiable {
    let id = UUID()
    let title: String
    let isMet: Bool
}

private struct ValidationMessage: View {
    let text: String

    var body: some View {
        Label(text, systemImage: "exclamationmark.circle")
            .font(.caption)
            .foregroundStyle(.red)
            .accessibilityLabel(text)
    }
}

struct LoginDashboardScreen: View {
    let email: String
    @State private var isComponentDemoPresented = false

    var body: some View {
        List {
            Section {
                Label("Signed in as \(email)", systemImage: "person.crop.circle.badge.checkmark")
                Label("Validation completed", systemImage: "checkmark.seal.fill")
            }

            Section("Next Page") {
                Button {
                    isComponentDemoPresented = true
                } label: {
                    Label("Open Component Demo", systemImage: "square.grid.2x2")
                }
                .accessibilityIdentifier("openComponentDemoLink")
            }
        }
        .navigationTitle("Dashboard")
        .navigationBarBackButtonHidden(true)
        .navigationDestination(isPresented: $isComponentDemoPresented) {
            ContentView()
                .navigationBarBackButtonHidden(true)
        }
    }
}

struct LoginScreen_Previews: PreviewProvider {
    static var previews: some View {
        LoginScreen()
    }
}
