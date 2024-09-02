package com.deepdrilling.blockentities.sludgepump;

import com.deepdrilling.FluidTankAssociations;
import com.deepdrilling.fluid.CFluidType;
import com.deepdrilling.fluid.SingleTank;
import org.jetbrains.annotations.Nullable;

public class SludgePumpTank extends SingleTank {
    public SludgePumpTank(long capacity) {
        super(capacity);
    }

    public long forceInsert(CFluidType insertedType, long forcedAmount) {
        this.type = insertedType;
        this.amount += FluidTankAssociations.mbToLoaderUnits(forcedAmount);
        return forcedAmount;
    }

    @Override
    public long insert(CFluidType insertedType, long maxAmount, boolean simulate, @Nullable Runnable beforeApply) {
        return 0;
    }
}
