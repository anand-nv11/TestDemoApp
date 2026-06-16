package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ScreenshotUtil {

    public static void saveScreenshot(File sourceFile, String testName) throws IOException {
        Path screenshotDir = Path.of("screenshots");
        Files.createDirectories(screenshotDir);

        Path destination = screenshotDir.resolve(testName + "_" + System.currentTimeMillis() + ".png");
        Files.copy(sourceFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

        System.out.println("Screenshot saved at: " + destination.toAbsolutePath());
    }
}
