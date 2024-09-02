package com.deepdrilling.fluid;

import com.deepdrilling.FluidTankAssociations;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

public class SingleTank {
    public long amount;
    public final long capacity;
    public CFluidType type = CFluidType.BLANK;

    public SingleTank(long capacity) {
        this.capacity = FluidTankAssociations.mbToLoaderUnits(capacity);
    }

    /**
     * @return fluid amount in millibuckets. <strong>Will round away droplets in fabric</strong>
     */
    public long getMbAmount() {
        return FluidTankAssociations.loaderUnitsToMB(this.amount);
    }

    static long calculateInsert(SingleTank tank, CFluidType insertedType, long maxAmount) {
        if (insertedType.equals(tank.type) || tank.type.isBlank()) {
            return Math.min(maxAmount, tank.capacity - tank.amount);
        }
        return 0;
    }

    static void applyInsert(SingleTank tank, CFluidType insertedType, long insertedAmount) {
        tank.type = insertedType;
        tank.amount += insertedAmount;
    }

    static long calculateExtract(SingleTank tank, CFluidType extractedType, long maxAmount) {
        if (extractedType.equals(tank.type)) {
            return Math.min(maxAmount, tank.amount);
        }
        return 0;
    }

    static void applyExtract(SingleTank tank, long extractedAmount) {
        tank.amount -= extractedAmount;
        if (tank.amount == 0) {
            tank.type = CFluidType.BLANK;
        }
    }

    /**
     * Performs an insertion of a fluid
     * @param insertedType The type of fluid
     * @param maxAmount    The amount attempted to insert
     * @param simulate     Whether this action shouldn't be applied - always false on fabric, which uses snapshots and rollback
     * @param beforeApply  Used for fabric transaction snapshotting
     * @return The amount actually inserted
     */
    public long insert(CFluidType insertedType, long maxAmount, boolean simulate, @Nullable Runnable beforeApply) {
        long v = calculateInsert(this, insertedType, maxAmount);
        if (!simulate) {
            if (beforeApply != null)
                beforeApply.run();
            applyInsert(this, insertedType, v);
        }
        return v;
    }

    public final long insert(CFluidType insertedType, long maxAmount, boolean simulate) {
        return insert(insertedType, maxAmount, simulate, null);
    }

    /**
     * Performs an extraction of a fluid
     * @param extractedType The type of fluid
     * @param maxAmount     The amount attempted to extract
     * @param simulate      Whether this action shouldn't be applied - always false on fabric, which uses snapshots and rollback
     * @param beforeApply   Used for fabric transaction snapshotting
     * @return The amount actually extracted
     */
    public long extract(CFluidType extractedType, long maxAmount, boolean simulate, @Nullable Runnable beforeApply) {
        long v = calculateExtract(this, extractedType, maxAmount);
        if (!simulate) {
            if (beforeApply != null)
                beforeApply.run();
            applyExtract(this, v);
        }
        return v;
    }

    public final long extract(CFluidType insertedType, long maxAmount, boolean simulate) {
        return extract(insertedType, maxAmount, simulate, null);
    }

    public void read(CompoundTag tag) {
        this.amount = tag.getInt("Amount");
        this.type = CFluidType.read(tag.getCompound("Variant"));
    }

    public CompoundTag write() {
        CompoundTag tag = new CompoundTag();
        tag.putLong("Amount", this.amount);
        tag.put("Variant", type.write());
        return tag;
    }
}
