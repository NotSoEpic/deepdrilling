package com.deepdrilling.blocks;

import com.simibubi.create.content.contraptions.ContraptionMovementSetting;
import net.minecraft.world.level.block.Block;

public class OreNodeBlock extends Block implements ContraptionMovementSetting.IMovementSettingProvider {
    public OreNodeBlock(Properties properties) {
        super(properties);
    }

    @Override
    public ContraptionMovementSetting getContraptionMovementSetting() {
        return ContraptionMovementSetting.UNMOVABLE;
    }
}
