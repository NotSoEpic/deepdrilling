package com.deepdrilling.ponders;

import com.simibubi.create.foundation.ponder.PonderPalette;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import net.minecraft.world.phys.AABB;

public class DrillingScenes {
    public static void nodeLocating(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("node_locating", "Locating Ore Nodes");
        scene.configureBasePlate(1, 1, 9);
        AABB depositBB = new AABB(util.grid.at(4, 2, 4)).inflate(2, 1, 2);
        AABB nodeBB = new AABB(util.grid.at(4, 0, 4)).inflate(1, 0, 1);

        scene.overlay.chaseBoundingBoxOutline(PonderPalette.WHITE, new Object(), depositBB, 80);
        scene.overlay.showText(80)
                .text("Rarely, deposits of stone and ore may be found on the surface")
                .placeNearTarget()
                .pointAt(util.vector.topOf(4, 3, 4));
        scene.idle(90);
        scene.overlay.showText(60)
                .text("Deep underground beneath these...")
                .placeNearTarget()
                .pointAt(util.vector.topOf(4, 3, 4));
        scene.idle(60);
        //scene.world.hideSection(util.select.fromTo(2, 1, 2, 6, 4, 6), Direction.UP);
        scene.idle(10);
        scene.overlay.showText(60)
                .text("... Are ore nodes")
                .placeNearTarget()
                .pointAt(util.vector.topOf(4, 0, 4));
        scene.overlay.chaseBoundingBoxOutline(PonderPalette.RED, new Object(), nodeBB, 60);
    }
}
