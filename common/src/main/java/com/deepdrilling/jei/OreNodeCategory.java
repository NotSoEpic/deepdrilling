package com.deepdrilling.jei;

import com.simibubi.create.compat.jei.category.CreateRecipeCategory;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.awt.*;

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

        int x = 70;
        for (Item item : recipe.lootEntry.earth()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, x, 14)
                    .setBackground(getRenderedSlot(0), -1, -1)
                    .addItemStack(new ItemStack(item));
            x += 19;
        }
        x = 70;
        for (Item item : recipe.lootEntry.common()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, x, 44)
                    .setBackground(getRenderedSlot(0), -1, -1)
                    .addItemStack(new ItemStack(item));
            x += 19;
        }
        x = 70;
        for (Item item : recipe.lootEntry.rare()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, x, 74)
                    .setBackground(getRenderedSlot(0), -1, -1)
                    .addItemStack(new ItemStack(item));
            x += 19;
        }
    }

    @Override
    public void draw(FakeOreNodeRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);

        if (recipe.nodeBlock.getItem() instanceof BlockItem blockItem) {
            drill.draw(guiGraphics, 30, 60, blockItem.getBlock().defaultBlockState());
        } else {
            drill.draw(guiGraphics, 30, 60);
        }

        guiGraphics.drawString(Minecraft.getInstance().font, Component.translatable("deepdrilling.loot.earth"), 70, 4, Color.WHITE.getRGB());
        guiGraphics.drawString(Minecraft.getInstance().font, Component.translatable("deepdrilling.loot.common"), 70, 34, Color.WHITE.getRGB());
        guiGraphics.drawString(Minecraft.getInstance().font, Component.translatable("deepdrilling.loot.rare"), 70, 64, Color.WHITE.getRGB());
    }
}
