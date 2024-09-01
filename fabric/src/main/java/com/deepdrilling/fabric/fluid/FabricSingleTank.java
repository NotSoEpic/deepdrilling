package com.deepdrilling.fabric.fluid;

import com.deepdrilling.fluid.SingleTank;
import com.deepdrilling.fluid.CFluidType;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.ResourceAmount;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

@SuppressWarnings("UnstableApiUsage")
public class FabricSingleTank extends SingleVariantStorage<FluidVariant> {
    private final SingleTank tank;
    public FabricSingleTank(SingleTank tank) {
        this.tank = tank;
    }

    public static FluidVariant fromCommon(CFluidType type) {
        return FluidVariant.of(type.fluid, type.tag);
    }

    public static CFluidType toCommon(FluidVariant variant) {
        return new CFluidType(variant.getFluid(), variant.copyNbt());
    }

    @Override
    protected FluidVariant getBlankVariant() {
        return FluidVariant.blank();
    }

    @Override
    public long insert(FluidVariant insertedVariant, long maxAmount, TransactionContext transaction) {
        return tank.insert(toCommon(insertedVariant), maxAmount, false, () -> updateSnapshots(transaction));
    }

    @Override
    public long extract(FluidVariant extractedVariant, long maxAmount, TransactionContext transaction) {
        return tank.extract(toCommon(extractedVariant), maxAmount, false, () -> updateSnapshots(transaction));
    }

    @Override
    protected ResourceAmount<FluidVariant> createSnapshot() {
        return new ResourceAmount<>(fromCommon(tank.type), tank.amount);
    }

    @Override
    protected void readSnapshot(ResourceAmount<FluidVariant> snapshot) {
        tank.type = toCommon(snapshot.resource());
        tank.amount = snapshot.amount();
    }

    @Override
    public boolean isResourceBlank() {
        return getResource().isBlank();
    }

    @Override
    protected long getCapacity(FluidVariant variant) {
        return tank.capacity;
    }

    @Override
    public long getAmount() {
        return tank.amount;
    }

    @Override
    public FluidVariant getResource() {
        return fromCommon(tank.type);
    }
}
