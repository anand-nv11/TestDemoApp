# SwiftUIDemoAllComponents

SwiftUIDemoAllComponents is a learning project that demonstrates common SwiftUI UI components in one navigable app. The app uses a `TabView` root, a searchable component catalog, and a `NavigationStack` detail flow for each component.

## What This App Includes

The main screen lists SwiftUI component demos. Tapping a row opens a detail screen with multiple UI variations for the selected component.

Included components:

- Box
- Button
- CalendarPicker
- Checkbox
- ClockPicker
- ColorWell
- DatePicker
- FontPicker
- HelpButton
- Image
- Label
- Level / Progress View
- PopUp / Alert / Sheet
- Radio Button
- Separator / Divider
- Slider
- ScrollView
- Switch / Toggle
- Tabs / TabView
- TextField
- TextView / TextEditor
- TimePicker

## Project Structure

```text
SwiftUIDemoAllComponents/
├── SwiftUIDemoAllComponents/
│   ├── Assets.xcassets
│   ├── ContentView.swift
│   ├── SwiftUIDemoAllComponentsApp.swift
│   ├── Models/
│   │   └── AppDemoModels.swift
│   ├── ViewModels/
│   │   └── AppDemoFormViewModel.swift
│   └── Views/
│       ├── Components/
│       │   └── AppReusableComponents.swift
│       └── Screens/
│           └── AppComponentScreens.swift
├── SwiftUIDemoAllComponentsTests/
└── SwiftUIDemoAllComponentsUITests/
```

## Important Implementation Note

Some support types are currently also defined inside `ContentView.swift`. This was done because the new separate Swift files were excluded from the app target by Xcode target-membership metadata. Keeping the required types in `ContentView.swift` makes the app compile immediately because `ContentView.swift` is already included in the app target.

When target membership is fixed in Xcode, the duplicated support code can be moved back into the separated files:

- `Models/AppDemoModels.swift`
- `ViewModels/AppDemoFormViewModel.swift`
- `Views/Components/AppReusableComponents.swift`
- `Views/Screens/AppComponentScreens.swift`

To fix target membership manually:

1. Open the project in Xcode.
2. Select each separated Swift file.
3. Open the File Inspector.
4. Under Target Membership, check `SwiftUIDemoAllComponents`.
5. Remove duplicated definitions from `ContentView.swift` only after the app target can see the separated files.

## App Flow

1. Launch the app.
2. Open the `Components` tab.
3. Search or scroll the component list.
4. Tap any component row.
5. The app navigates to a detail screen for that component.
6. The detail screen shows multiple UI variations for the selected component.

There is also a `Guide` tab with SwiftUI best practices, common interview questions, common mistakes, performance tips, and testing notes.

## Component Detail Screens

Each component screen focuses on practical UI variations instead of generic examples.

For example, the Button screen includes:

- Default button
- Prominent button
- Bordered button
- Destructive button
- Icon button

Other screens show similar multiple variations, such as:

- Different DatePicker styles
- Compact and wheel time pickers
- Multiple Toggle and checkbox styles
- ColorPicker with live preview
- Multiple font designs
- ProgressView linear and circular styles
- Alert and sheet presentation buttons
- Segmented picker and custom radio rows
- Horizontal and vertical ScrollView examples
- TextField and SecureField examples
- TextEditor with character count

## SwiftUI Concepts Demonstrated

- `NavigationStack`
- `NavigationLink`
- `TabView`
- `List`
- `ScrollView`
- `LazyVStack`
- `@State`
- `@Binding` style data flow
- Search with `.searchable`
- Alerts with `.alert`
- Sheets with `.sheet`
- Built-in control styles
- SF Symbols
- Light and Dark Mode friendly colors
- Accessibility labels and identifiers

## Design Notes

The UI uses system colors, materials, SF Symbols, rounded cards, and adaptive backgrounds. These choices keep the app readable in both Light Mode and Dark Mode.

The component catalog uses a standard `NavigationLink` row so iOS shows a single built-in forward arrow. No custom extra arrow is added.

## Accessibility

The app includes accessibility improvements such as:

- Meaningful labels for rows and actions
- Hidden decorative icons where needed
- Accessibility identifiers for UI tests
- Native SwiftUI controls where possible
- Dynamic system colors for contrast

Recommended additions for a production app:

- Test with VoiceOver
- Test with larger Dynamic Type sizes
- Add accessibility hints for complex controls
- Avoid icon-only actions unless they have clear accessibility labels

## Testing

The project includes:

- Unit test target: `SwiftUIDemoAllComponentsTests`
- UI test target: `SwiftUIDemoAllComponentsUITests`

Suggested test coverage:

- Verify component repository count and titles
- Verify form validation rules
- Verify navigation from catalog row to detail screen
- Verify guide tab opens
- Verify alert and sheet buttons present expected UI

## Common SwiftUI Best Practices

- Use `NavigationStack` instead of older `NavigationView`.
- Keep state as close as possible to the view that owns it.
- Move validation and business rules into view models when the logic grows.
- Prefer native SwiftUI controls before creating custom controls.
- Use `List` for large row-based collections.
- Use `ScrollView` with `LazyVStack` for custom scrolling layouts.
- Use system colors and materials for Dark Mode support.
- Add accessibility labels for icon-only buttons.
- Keep previews for important screens and reusable components.

## Common Mistakes To Avoid

- Duplicating model definitions across files after target membership is fixed.
- Using fixed widths that break on smaller devices.
- Building custom controls when native controls already solve the problem.
- Forgetting to validate user input.
- Forgetting accessibility labels for custom buttons.
- Adding multiple arrows to `NavigationLink` rows.
- Putting heavy logic directly inside SwiftUI view bodies.

## Performance Tips

- Use `LazyVStack` or `List` for long content.
- Keep `ForEach` identifiers stable.
- Avoid expensive calculations inside repeated view bodies.
- Keep images sized appropriately.
- Use Instruments for real performance problems.

## Requirements

- Xcode with SwiftUI support
- iOS Simulator or physical iOS device
- Swift 5.9 or newer recommended

## Running The App

1. Open `SwiftUIDemoAllComponents.xcodeproj` in Xcode.
2. Select the `SwiftUIDemoAllComponents` scheme.
3. Choose an iOS Simulator.
4. Build and run.

If Xcode shows a signing error, select a development team in Signing & Capabilities or use a simulator build configuration that does not require device signing.

