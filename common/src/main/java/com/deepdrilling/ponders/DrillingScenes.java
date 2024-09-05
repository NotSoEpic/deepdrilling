package com.deepdrilling.ponders;

import com.deepdrilling.blockentities.drillhead.DDrillHeads;
import com.deepdrilling.blocks.DrillHeadBlock;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.ponder.ElementLink;
import com.simibubi.create.foundation.ponder.PonderWorld;
import com.simibubi.create.foundation.ponder.SceneBuilder;
import com.simibubi.create.foundation.ponder.SceneBuildingUtil;
import com.simibubi.create.foundation.ponder.element.WorldSectionElement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class DrillingScenes {
    public static void loopingBlockBreakingProgress(SceneBuilder builder, BlockPos pos) {
        builder.addInstruction(scene -> {
            PonderWorld world = scene.getWorld();
            int progress = world.getBlockBreakingProgressions()
                    .getOrDefault(pos, -1) + 1;
            if (progress == 9) {
                world.setBlockBreakingProgress(pos, 0);
            } else
                world.setBlockBreakingProgress(pos, progress + 1);
        });
    }

    public static void drillBasics(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("drill_basics", "Using Deep Drills");
        scene.configureBasePlate(0, 0, 5);

        scene.world.setKineticSpeed(util.select.everywhere(), 16);

        scene.world.showSection(util.select.fromTo(0, 0, 0, 4, 0, 4), Direction.UP);
        scene.idle(3);
        scene.world.showSection(util.select.fromTo(2, 0, 5, 2, 3, 5), Direction.NORTH);
        scene.idle(3);
        scene.world.showSection(util.select.fromTo(2, 3, 4, 2, 3, 2), Direction.DOWN);
        scene.idle(3);
        scene.world.showSection(util.select.position(2, 2, 2), Direction.SOUTH);
        scene.idle(3);
        scene.world.showSection(util.select.position(2, 1, 2), Direction.SOUTH);

        BlockPos breakingPos = util.grid.at(2, 0, 2);

        int totalTicks = 0;
        for (int i = 0; i < 15; i++) {
            scene.idle(10);
            loopingBlockBreakingProgress(scene, breakingPos);
            if (i == 1) {
                scene.overlay.showText(80)
                        .text("When given a drill head and Rotational Force, a Deep Drill will begin extracting resources from Ore Nodes")
                        .pointAt(util.vector.topOf(breakingPos))
                        .attachKeyFrame()
                        .placeNearTarget();
            }
            if (totalTicks == 9) {
                totalTicks = 0;
                scene.world.createItemEntity(util.vector.topOf(breakingPos), util.vector.of(-0.15f, 0.15f, 0),
                        new ItemStack(AllItems.CRUSHED_IRON.get()));
            } else {
                totalTicks++;
            }
        }

        scene.world.replaceBlocks(util.select.position(2, 1, 2),
                DDrillHeads.BRASS.getDefaultState().setValue(DrillHeadBlock.FACING, Direction.DOWN), true);
        for (int i = 0; i < 15; i++) {
            scene.idle(6);
            loopingBlockBreakingProgress(scene, breakingPos);
            if (i == 1) {
                scene.overlay.showText(80)
                        .text("The process can be sped up with better Drill Heads...")
                        .pointAt(util.vector.topOf(breakingPos))
                        .attachKeyFrame()
                        .placeNearTarget();
            }
            if (totalTicks == 9) {
                totalTicks = 0;
                scene.world.createItemEntity(util.vector.topOf(breakingPos), util.vector.of(-0.15f, 0.15f, 0),
                        new ItemStack(AllItems.CRUSHED_IRON.get()));
            } else {
                totalTicks++;
            }
        }

        scene.world.setKineticSpeed(util.select.everywhere(), 64);
        for (int i = 0; i < 30; i++) {
            scene.idle(3);
            loopingBlockBreakingProgress(scene, breakingPos);
            if (i == 1) {
                scene.overlay.showText(60)
                        .text("... or increasing its Rotational Input")
                        .pointAt(util.vector.topOf(breakingPos))
                        .attachKeyFrame()
                        .placeNearTarget();
            }
            if (totalTicks == 9) {
                totalTicks = 0;
                scene.world.createItemEntity(util.vector.topOf(breakingPos), util.vector.of(-0.15f, 0.15f, 0),
                        new ItemStack(AllItems.CRUSHED_IRON.get()));
            } else {
                totalTicks++;
            }
        }
    }

    public static void drillModules(SceneBuilder scene, SceneBuildingUtil util) {
        scene.title("drill_modules", "Using Modules");
        scene.configureBasePlate(0, 0, 5);
        scene.world.showSection(util.select.fromTo(0, 0, 0, 4, 0, 4), Direction.UP);

        scene.world.setKineticSpeed(util.select.everywhere(), 16);

        ElementLink<WorldSectionElement> side_shaft_top = scene.world
                .showIndependentSection(util.select.fromTo(2, 3, 5, 2, 8, 5), Direction.NORTH);
        scene.world.moveSection(side_shaft_top, new Vec3(0, -5, 0), 0);
        scene.idle(5);

        ElementLink<WorldSectionElement> top_shaft = scene.world
                .showIndependentSection(util.select.fromTo(2, 8, 4, 2, 8, 2), Direction.DOWN);
        scene.world.moveSection(top_shaft, new Vec3(0, -5, 0), 0);
        scene.idle(5);

        scene.world.showSection(util.select.position(2, 2, 2), Direction.SOUTH);
        scene.idle(5);

        scene.world.showSection(util.select.position(2, 1, 2), Direction.SOUTH);
        scene.idle(5);

        BlockPos breakingPos = util.grid.at(2, 0, 2);
        int totalTicks = 0;
        for (int i = 0; i < 15; i++) {
            scene.idle(10);
            loopingBlockBreakingProgress(scene, breakingPos);
            if (i == 1) {
                scene.overlay.showText(80)
                        .text("To prevent items from being thrown on the ground a Collection Filter can be added")
                        .pointAt(util.vector.topOf(breakingPos))
                        .attachKeyFrame()
                        .placeNearTarget();
            }
            if (totalTicks == 9) {
                totalTicks = 0;
                scene.world.createItemEntity(util.vector.topOf(breakingPos), util.vector.of(-0.15f, 0.15f, 0),
                        new ItemStack(AllItems.CRUSHED_IRON.get()));
            } else {
                totalTicks++;
            }
        }

        scene.world.setKineticSpeed(util.select.fromTo(2, 1, 2, 2, 2, 2), 0);
        scene.world.moveSection(side_shaft_top, new Vec3(0, 1, 0), 5);
        scene.world.moveSection(top_shaft, new Vec3(0, 1, 0), 5);
        scene.idle(5);

        scene.world.showSection(util.select.position(2, 3, 2), Direction.SOUTH);
        scene.idle(5);

        scene.world.setKineticSpeed(util.select.everywhere(), 16);
        for (int i = 0; i < 15; i++) {
            scene.idle(10);
            loopingBlockBreakingProgress(scene, breakingPos);
            if (i == 5) {
                scene.overlay.showText(80)
                        .text("Results are stored in its inventory")
                        .pointAt(util.vector.blockSurface(util.grid.at(2, 3, 2), Direction.WEST))
                        .attachKeyFrame()
                        .placeNearTarget();
            }
            if (totalTicks == 9) {
                totalTicks = 0;
            } else {
                totalTicks++;
            }
        }

        scene.world.showSection(util.select.position(1, 3, 2), Direction.EAST);
        scene.overlay.showText(80)
                .text("And can be automatically extracted")
                .pointAt(util.vector.blockSurface(util.grid.at(1, 3, 2), Direction.EAST))
                .attachKeyFrame()
                .placeNearTarget();
        scene.idle(5);

        Vec3 sideItemSpawn = util.vector.centerOf(1, 3, 2)
                .add(-0.15f, -0.45f, 0);
        scene.world.flapFunnel(util.grid.at(1, 3, 2), true);
        scene.world.createItemEntity(sideItemSpawn, util.vector.of(0, 0, 0),
                new ItemStack(AllItems.CRUSHED_IRON.get()));
        scene.idle(10);
        scene.world.flapFunnel(util.grid.at(1, 3, 2), true);
        scene.world.createItemEntity(sideItemSpawn, util.vector.of(0, 0, 0),
                new ItemStack(AllItems.CRUSHED_IRON.get()));

        for (int i = 0; i < 10; i++) {
            scene.idle(10);
            loopingBlockBreakingProgress(scene, breakingPos);
            if (totalTicks == 9) {
                totalTicks = 0;
                scene.world.flapFunnel(util.grid.at(1, 3, 2), true);
                scene.world.createItemEntity(sideItemSpawn, util.vector.of(0, 0, 0),
                        new ItemStack(AllItems.CRUSHED_IRON.get()));
            } else {
                totalTicks++;
            }
        }

        scene.world.setKineticSpeed(util.select.fromTo(2, 1, 2, 2, 3, 2), 0);
        scene.world.moveSection(side_shaft_top, new Vec3(0, 1, 0), 5);
        scene.world.moveSection(top_shaft, new Vec3(0, 1, 0), 5);
        scene.idle(5);

        scene.world.moveSection(side_shaft_top, new Vec3(0, 3, 0), 15);
        scene.world.moveSection(top_shaft, new Vec3(0, 3, 0), 15);
        ElementLink<WorldSectionElement> side_shaft_bottom = scene.world
                .showIndependentSection(util.select.fromTo(2, 0, 5, 2, 2, 5), Direction.UP);
        scene.world.moveSection(side_shaft_bottom, new Vec3(0, -3, 0), 0);
        scene.world.moveSection(side_shaft_bottom, new Vec3(0, 3, 0), 15);
        scene.idle(15);

        scene.world.showSection(util.select.fromTo(2, 4, 2, 2, 7, 2), Direction.SOUTH);
        scene.world.setKineticSpeed(util.select.everywhere(), 16);
        for (int i = 0; i < 40; i++) {
            scene.idle(2);
            loopingBlockBreakingProgress(scene, breakingPos);
            if (i == 15) {
                scene.overlay.showText(80)
                        .text("Up to 5 modules can be attached to one Deep Drill")
                        .pointAt(util.vector.blockSurface(util.grid.at(2, 4, 2), Direction.WEST))
                        .attachKeyFrame()
                        .placeNearTarget();
            }
            if (totalTicks == 9) {
                totalTicks = 0;
                scene.world.flapFunnel(util.grid.at(1, 3, 2), true);
                scene.world.createItemEntity(sideItemSpawn, util.vector.of(0, 0, 0),
                        new ItemStack(AllItems.CRUSHED_IRON.get()));
            } else {
                totalTicks++;
            }
        }

        scene.world.setBlock(util.grid.at(2, 1, 2), Blocks.AIR.defaultBlockState(), true);
    }
}
