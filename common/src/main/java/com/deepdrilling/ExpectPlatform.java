package com.deepdrilling;

public class ExpectPlatform {
    /**
     * an example of {@link dev.architectury.injectables.annotations.ExpectPlatform}.
     * <p>
     * This must be a <b>public static</b> method. The platform-implemented solution must be placed under a
     * platform sub-package, with its class suffixed with {@code Impl}.
     * <p>
     * Example:
     * Expect: com.beeisyou.deepdrilling.examplemod.ExampleExpectPlatform#platformName()
     * Actual Fabric: net.examplemod.fabric.ExampleExpectPlatformImpl#platformName()
     * Actual Forge: net.examplemod.forge.ExampleExpectPlatformImpl#platformName()
     * <p>
     * <a href="https://plugins.jetbrains.com/plugin/16210-architectury">You should also get the IntelliJ plugin to help with @ExpectPlatform.</a>
     */
    @dev.architectury.injectables.annotations.ExpectPlatform
    public static String platformName() {
        // Just throw an error, the content should get replaced at runtime.
        throw new AssertionError();
    }
}
