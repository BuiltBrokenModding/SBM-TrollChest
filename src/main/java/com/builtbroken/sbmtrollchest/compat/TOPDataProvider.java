package com.builtbroken.sbmtrollchest.compat;

import java.util.function.Function;

import javax.annotation.Nullable;

import com.builtbroken.sbmtrollchest.TrollChest;
import com.builtbroken.sbmtrollchest.block.BlockTrollChest;

import mcjty.theoneprobe.api.IBlockDisplayOverride;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class TOPDataProvider implements Function<ITheOneProbe, Void>
{
    private final String text = TextFormatting.BLUE.toString() + TextFormatting.ITALIC.toString()  + "Minecraft";

    @Nullable
    @Override
    public Void apply(ITheOneProbe theOneProbe)
    {
        theOneProbe.registerBlockDisplayOverride(new IBlockDisplayOverride() {
            @Override
            public boolean overrideStandardInfo(ProbeMode mode, IProbeInfo probeInfo, EntityPlayer player, World world, IBlockState blockState, IProbeHitData data)
            {
                if(blockState.getBlock() instanceof BlockTrollChest)
                {
                    probeInfo.horizontal()
                    .item(TrollChest.CHEST_STACK)
                    .vertical()
                    .itemLabel(TrollChest.CHEST_STACK)
                    .text(text);
                    return true;
                }

                return false;
            }
        });
        return null;
    }
}