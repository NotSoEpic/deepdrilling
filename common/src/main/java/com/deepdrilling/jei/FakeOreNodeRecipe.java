package com.deepdrilling.jei;

import com.deepdrilling.DrillMod;
import com.deepdrilling.nodes.OreNode;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

// this is bad and hacky wawa
public class FakeOreNodeRecipe implements Recipe<CraftingContainer> {
    public final ItemStack nodeBlock;
    public final OreNode nodeTables;
    public FakeOreNodeRecipe(Block nodeBlock, OreNode nodeTables) {
        this.nodeBlock = nodeBlock.asItem().getDefaultInstance();
        // it was at that moment i realised loot tables are not even loaded on the client at all...
        this.nodeTables = nodeTables;
    }

    public static final mezz.jei.api.recipe.RecipeType<FakeOreNodeRecipe> RECIPE_TYPE =
            mezz.jei.api.recipe.RecipeType.create(DrillMod.MOD_ID, "fake_ore_node_recipe", FakeOreNodeRecipe.class);
    public static class Type implements RecipeType<FakeOreNodeRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        return false;
    }

    @Override
    public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return DrillMod.id("fake_recipe");
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return Type.INSTANCE;
    }
}
