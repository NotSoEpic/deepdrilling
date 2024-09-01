package com.deepdrilling.forge.fluid;

import com.deepdrilling.fluid.CFluidType;
import com.deepdrilling.fluid.SingleTank;
import net.minecraft.util.Tuple;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

public class ForgeSingleTank extends FluidTank {
    private final SingleTank tank;
    public ForgeSingleTank(SingleTank tank) {
        super((int) tank.capacity);
        this.tank = tank;
        this.fluid = new FluidStack(tank.type.fluid, (int) tank.amount, tank.type.tag);
    }

    public static FluidStack fromCommon(CFluidType type, int amount) {
        return new FluidStack(type.fluid, amount, type.tag);
    }

    public static Tuple<CFluidType, Integer> toCommon(FluidStack variant) {
        return new Tuple<>(new CFluidType(variant.getFluid(), variant.getTag()), variant.getAmount());
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        Tuple<CFluidType, Integer> fluidStack = toCommon(resource);
        return (int) tank.insert(fluidStack.getA(), fluidStack.getB(), action.simulate());
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        return fromCommon(tank.type, (int) tank.extract(tank.type, maxDrain, action.simulate()));
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        Tuple<CFluidType, Integer> fluidStack = toCommon(resource);
        return fromCommon(fluidStack.getA(), (int) tank.extract(fluidStack.getA(), fluidStack.getB(), action.simulate()));
    }

    @Override
    public @NotNull FluidStack getFluid() {
        return fromCommon(tank.type, (int) tank.amount);
    }

    @Override
    public void setFluid(FluidStack stack) {
        Tuple<CFluidType, Integer> fluidStack = toCommon(stack);
        tank.type = fluidStack.getA();
        tank.amount = fluidStack.getB();
    }

    @Override
    public int getFluidAmount() {
        return (int) tank.amount;
    }

    @Override
    public boolean isEmpty() {
        return tank.amount == 0;
    }

    @Override
    public int getSpace() {
        return (int) (tank.capacity - tank.amount);
    }
}
