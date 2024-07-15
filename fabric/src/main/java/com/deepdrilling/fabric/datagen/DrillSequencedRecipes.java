package com.deepdrilling.fabric.datagen;

import com.deepdrilling.DItems;
import com.deepdrilling.DrillMod;
import com.deepdrilling.blockentities.drillhead.DDrillHeads;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllItems;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.kinetics.saw.CuttingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;
import com.simibubi.create.foundation.data.recipe.CreateRecipeProvider;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;

import java.util.function.UnaryOperator;

public class DrillSequencedRecipes extends CreateRecipeProvider {
    GeneratedRecipe

    COPPER_DRILL_HEAD = createSequence("copper_drill_head", b -> b.require(AllBlocks.INDUSTRIAL_IRON_BLOCK)
            .transitionTo(DItems.INCOMPLETE_COPPER_DRILL_HEAD)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(AllItems.COPPER_SHEET))
            .addStep(CuttingRecipe::new, rb -> rb)
            .addStep(PressingRecipe::new, rb -> rb)
            .loops(3)
            .addOutput(DDrillHeads.COPPER, 100)),

    BRASS_DRILL_HEAD = createSequence("brass_drill_head", b -> b.require(AllBlocks.INDUSTRIAL_IRON_BLOCK)
            .transitionTo(DItems.INCOMPLETE_BRASS_DRILL_HEAD)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(AllItems.BRASS_SHEET))
            .addStep(CuttingRecipe::new, rb -> rb)
            .addStep(DeployerApplicationRecipe::new, rb -> rb.require(AllItems.STURDY_SHEET))
            .addStep(CuttingRecipe::new, rb -> rb)
            .addStep(PressingRecipe::new, rb -> rb)
            .loops(5)
            .addOutput(DDrillHeads.BRASS, 100));

    public DrillSequencedRecipes(FabricDataOutput output) {
        super(output);
    }

    private GeneratedRecipe createSequence(String name, UnaryOperator<SequencedAssemblyRecipeBuilder> transform) {
        GeneratedRecipe generatedRecipe =
                c -> transform.apply(new SequencedAssemblyRecipeBuilder(DrillMod.id(name)))
                        .build(c);
        all.add(generatedRecipe);
        return generatedRecipe;
    }
}
