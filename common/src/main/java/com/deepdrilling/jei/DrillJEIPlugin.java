package com.deepdrilling.jei;

import com.deepdrilling.DrillMod;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import net.minecraft.resources.ResourceLocation;

@JeiPlugin
public class DrillJEIPlugin implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return DrillMod.id("ore_node");
    }

    /*@Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new OreNodeCategory(new CreateRecipeCategory.Info<>(
                FakeOreNodeRecipe.RECIPE_TYPE, Lang.translate("deepdrilling.recipe.ore_node").component(), new EmptyBackground(177, 100),
                new ItemIcon(DDrillHeads.ANDESITE::asStack), ArrayList::new, List.of()
        )));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        List<FakeOreNodeRecipe> fakeRecipes = new ArrayList<>();
        OreNodes.getNodes().forEach((block, oreNode) -> {
            fakeRecipes.add(new FakeOreNodeRecipe(block, OreNode.EMPTY));
        });
        registration.addRecipes(FakeOreNodeRecipe.RECIPE_TYPE, fakeRecipes);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        DDrillHeads.knownDrillHeads.forEach(drillBlockEntry -> {
            registration.addRecipeCatalyst(drillBlockEntry.asStack(), FakeOreNodeRecipe.RECIPE_TYPE);
        });
    }*/
}
