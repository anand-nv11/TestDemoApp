# AppiumTests Ready To Push

Copy these folders into your existing GitHub repository root:

- `AppiumTests`
- `.github/workflows/ios-appium.yml`

Expected final structure:

```text
TestDemoApp
├── .github
│   └── workflows
│       └── ios-appium.yml
├── SwiftUIDemoAllComponents.xcodeproj
├── SwiftUIDemoAllComponents
├── SwiftUIDemoAllComponentsTests
├── SwiftUIDemoAllComponentsUITests
└── AppiumTests
    ├── pom.xml
    ├── testng.xml
    ├── src/test/java/tests/IOSAppiumTest.java
    ├── src/test/java/utils/ScreenshotUtil.java
    ├── screenshots
    └── allure-results
```

## Important

Update bundle id in `IOSAppiumTest.java` if needed:

```java
caps.setCapability("appium:bundleId", "test.com.SwiftUIDemoAllComponents");
```

Then push:

```bash
git add .
git commit -m "Add Appium automation with Allure"
git push origin main
```
