package com.deepdrilling.fluid;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public class CFluidType {
    public final Fluid fluid;
    @Nullable
    public final CompoundTag tag;

    public CFluidType(ResourceLocation type, @Nullable CompoundTag tag) {
        this.fluid = BuiltInRegistries.FLUID.get(type);
        this.tag = tag;
    }

    public CFluidType(Fluid type, @Nullable CompoundTag tag) {
        this.fluid = type;
        this.tag = tag;
    }

    public boolean isBlank() {
        return this.equals(BLANK);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof CFluidType other) {
            // both haves tag, or both no haves tag
            if ((tag == null) == (other.tag == null)) {
                return fluid.isSame(other.fluid) && (tag == null || tag.equals(other.tag));
            }
        }
        return false;
    }

    public static final CFluidType BLANK = new CFluidType(Fluids.EMPTY, null);

    public static CFluidType read(CompoundTag tag) {
        if (tag.contains("FluidTag")) {
            return new CFluidType(new ResourceLocation(tag.getString("Fluid")), tag.getCompound("FluidTag"));
        } else {
            return new CFluidType(new ResourceLocation(tag.getString("Fluid")), null);
        }
    }

    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Fluid", BuiltInRegistries.FLUID.getKey(fluid).toString());
        if (this.tag != null) {
            tag.put("FluidTag", this.tag);
        }
        return tag;
    }
}
