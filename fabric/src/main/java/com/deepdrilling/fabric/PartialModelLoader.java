package com.deepdrilling.fabric;

import com.deepdrilling.DPartialModels;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

public class PartialModelLoader {
    // Sometimes, For Some Reason, Some Mods cause PartialModels to lock out registering earlier than usual
    // this lock out happens *before* client init
    @Environment(EnvType.CLIENT)
    public static boolean wawa = DPartialModels.init();

    public static void doJavaShenanigans() {}
}
