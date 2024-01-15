package com.deepdrilling.fabric;

import net.fabricmc.loader.api.FabricLoader;

public class ExpectPlatformImpl {
	public static String platformName() {
		return FabricLoader.getInstance().isModLoaded("quilt_loader") ? "Quilt" : "Fabric";
	}
}
