package com.deepdrilling.jei;

import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.BlockItem;

public class OreNodeCategory extends CreateRecipeCategory<FakeOreNodeRecipe> {
    AnimatedDrill drill = new AnimatedDrill();

    public OreNodeCategory(Info<FakeOreNodeRecipe> info) {
        super(info);
    }

    @Override
    public RecipeType<FakeOreNodeRecipe> getRecipeType() {
        return FakeOreNodeRecipe.RECIPE_TYPE;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FakeOreNodeRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 5, 44)
                .setBackground(getRenderedSlot(), -1, -1)
                .addItemStack(recipe.nodeBlock);

//        builder.addSlot(RecipeIngredientRole.INPUT, 20, 0)
//                .setBackground(getRenderedSlot(), -1, -1)
//                .addItemStack(recipe.awaw);
    }

    @Override
    public void draw(FakeOreNodeRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);

        if (recipe.nodeBlock.getItem() instanceof BlockItem blockItem) {
            drill.draw(guiGraphics, 30, 60, blockItem.getBlock().defaultBlockState());
        } else {
            drill.draw(guiGraphics, 30, 60);
        }

    }
}
