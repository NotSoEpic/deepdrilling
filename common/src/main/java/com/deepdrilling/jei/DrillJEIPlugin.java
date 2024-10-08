package com.deepdrilling.jei;

import com.deepdrilling.DrillMod;
import com.deepdrilling.blockentities.drillhead.DDrillHeads;
import com.deepdrilling.nodes.LootParser;
import com.simibubi.create.compat.jei.EmptyBackground;
import com.simibubi.create.compat.jei.ItemIcon;
import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import com.simibubi.create.foundation.utility.Lang;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class DrillJEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return DrillMod.id("ore_node");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new OreNodeCategory(new CreateRecipeCategory.Info<>(
                FakeOreNodeRecipe.RECIPE_TYPE, Lang.translate("deepdrilling.recipe.ore_node").component(), new EmptyBackground(177, 100),
                new ItemIcon(DDrillHeads.ANDESITE::asStack), ArrayList::new, List.of()
        )));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {;
        registration.addRecipes(FakeOreNodeRecipe.RECIPE_TYPE,
                LootParser.knownTables.entrySet().stream()
                        .map(entry -> new FakeOreNodeRecipe(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList())
        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        DDrillHeads.knownDrillHeads.forEach(drillBlockEntry -> {
            registration.addRecipeCatalyst(drillBlockEntry.asStack(), FakeOreNodeRecipe.RECIPE_TYPE);
        });
    }
}
